package account.service.user;

import account.domain.user.UserAccessResponse;
import account.domain.user.UserDeletionResponse;
import account.domain.user.UserDto;
import account.mapper.UserMapper;
import account.model.user.*;
import account.repository.UserRepository;
import account.exception.admin.AdminDeletionException;
import account.exception.admin.InvalidRoleException;
import account.exception.admin.RoleUpdateException;
import account.exception.admin.UserNotFoundException;
import account.service.group.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GroupServiceImpl groupService;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, GroupServiceImpl groupService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.groupService = groupService;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllOrderById();
    }

    @Override
    @Transactional
    public UserDeletionResponse deleteUserByEmail(String email) {

        var user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (user.isAdmin()) {
            throw new AdminDeletionException("Can't remove ADMINISTRATOR role!");
        }
        userRepository.delete(user);
        return UserDeletionResponse.builder().user(email).status("Deleted successfully!").build();
    }

    @Override
    @Transactional
    public UserDto updateRole(RoleUpdateRequest request) {
        var user = userRepository.findByEmailIgnoreCase(request.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found!"));

        String roleFromRequest = String.format("ROLE_%s", request.getRole());

        if (Arrays.stream(Role.values()).anyMatch(role -> role.name().equals(roleFromRequest))) {
            Group group = groupService.findByRole(roleFromRequest).orElseThrow(() -> new InvalidRoleException("Role not found!"));

            switch (request.getOperation()) {
                case "GRANT":
                    return this.grant(user, group);
                case "REMOVE":
                    return revoke(user, group);
                default:
                    throw new RoleUpdateException("Unsupported operation!");
            }
        } else {
            throw new InvalidRoleException("Role not found!");
        }
    }

    @Override
    public UserDto grant(User user, Group group) {
        Role role = group.getRole();

        if ((user.isAdmin() && isBusinessRole().test(role)) || (!user.isAdmin() && Role.ROLE_ADMINISTRATOR.equals(role))) {
            throw new RoleUpdateException("The user cannot combine administrative and business roles!");
        } else {
            user.getGroups().add(group);
            user = this.userRepository.save(user);
            return userMapper.toDto(user);
        }
    }

    @Override
    public UserDto revoke(User user, Group group) {
        Role role = group.getRole();

        if (user.isAdmin() && Role.ROLE_ADMINISTRATOR.equals(role)) {
            throw new AdminDeletionException("Can't remove ADMINISTRATOR role!");
        }

        if (!user.hasRole(role)) {
            throw new RoleUpdateException("The user does not have a role!");
        }

        if (user.getGroups().size() == 1 && user.hasRole(role)) {
            throw new RoleUpdateException("The user must have at least one role!");
        }

        user.getGroups().remove(group);
        return userMapper.toDto(userRepository.save(user));
    }


    @Override
    @Transactional
    public User increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        user.setFailedAttempt(newFailAttempts);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
    }

    @Transactional
    @Override
    public void lock(User user) {
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserAccessResponse updateAccess(UserAccessUpdateRequest request) {
        var user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (user.isAdmin()) {
            throw new AdminDeletionException("Can't lock the ADMINISTRATOR!");
        }

        switch (request.getOperation()) {
            case "LOCK":
                lock(user);
                return new UserAccessResponse(String.format("User %s locked!", user.getEmail()));
            case "UNLOCK":
                user.setFailedAttempt(0);
                user.setAccountNonLocked(true);
                userRepository.save(user);
                return new UserAccessResponse(String.format("User %s unlocked!", user.getEmail()));
            default:
                throw new RoleUpdateException("Unsupported operation!");
        }
    }

    private Predicate<Role> isBusinessRole() {
        return role -> Role.ROLE_USER.equals(role) || Role.ROLE_ACCOUNTANT.equals(role) || Role.ROLE_AUDITOR.equals(role);
    }
}
