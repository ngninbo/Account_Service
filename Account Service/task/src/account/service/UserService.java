package account.service;

import account.domain.UserDeletionResponse;
import account.domain.UserDto;
import account.mapper.UserMapper;
import account.model.Group;
import account.model.Role;
import account.model.RoleUpdateRequest;
import account.model.User;
import account.repository.UserRepository;
import account.util.exception.AdminDeletionException;
import account.util.exception.InvalidRoleException;
import account.util.exception.RoleUpdateException;
import account.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class UserService implements  IUserService {

    private final UserRepository userRepository;
    private final GroupService groupService;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, GroupService groupService, UserMapper userMapper) {
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
    public UserDeletionResponse deleteUserByEmail(String email) throws UserNotFoundException, AdminDeletionException {

        var user = userRepository.findByEmailIgnoreCase(email);

        if (user.isPresent()) {
            User existingUser = user.get();
            if (existingUser.isAdmin()) {
                throw new AdminDeletionException("Can't remove ADMINISTRATOR role!");
            }
            userRepository.delete(existingUser);
            return UserDeletionResponse.builder().user(email).status("Deleted successfully!").build();
        } else {
            throw new UserNotFoundException("User not found!");
        }
    }

    @Override
    @Transactional
    public UserDto updateRole(RoleUpdateRequest request) throws UserNotFoundException, InvalidRoleException, RoleUpdateException, AdminDeletionException {
        var user = userRepository.findByEmailIgnoreCase(request.getEmail());
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        } else {
            User userToUpdate = user.get();

            String roleFromRequest = String.format("ROLE_%s", request.getRole());

            if (Arrays.stream(Role.values()).anyMatch(role -> role.name().equals(roleFromRequest))) {
                var group = groupService.findByRole(roleFromRequest);
                if (group.isEmpty()) {
                    throw new InvalidRoleException("Role not found!");
                }

                Group newGroup = group.get();

                switch (request.getOperation()) {
                    case "GRANT":
                        return this.grant(userToUpdate, newGroup);
                    case "REMOVE":
                        return revoke(userToUpdate, newGroup);
                    default:
                        throw new RoleUpdateException("Unsupported operation!");
                }
            } else {
                throw new InvalidRoleException("Role not found!");
            }
        }
    }

    @Override
    public UserDto grant(User user, Group group) throws RoleUpdateException {
        Role role = group.getRole();

        if ((user.isAdmin() && isBusinessRole().test(role))) {
            throw new RoleUpdateException("The user cannot combine administrative and business roles!");
        }

        if ((!user.isAdmin() && Role.ROLE_ADMINISTRATOR.equals(role))) {
            throw new RoleUpdateException("The user cannot combine administrative and business roles!");
        }

        else {
            user.getGroups().add(group);
            user = this.userRepository.save(user);
            return userMapper.toDto(user);
        }
    }

    @Override
    public UserDto revoke(User user, Group group) throws RoleUpdateException, AdminDeletionException {
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

    private Predicate<Role> isBusinessRole() {
        return role -> Role.ROLE_USER.equals(role) || Role.ROLE_ACCOUNTANT.equals(role);
    }
}
