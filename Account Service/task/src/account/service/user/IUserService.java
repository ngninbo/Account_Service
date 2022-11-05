package account.service.user;

import account.domain.user.UserAccessResponse;
import account.domain.user.UserDeletionResponse;
import account.domain.user.UserDto;
import account.model.user.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    User save(User user);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    UserDeletionResponse deleteUserByEmail(String email);

    UserDto updateRole(RoleUpdateRequest request);

    UserDto grant(User user, Group group);

    UserDto revoke(User user, Group group);

    User increaseFailedAttempts(User user);

    @Transactional
    void resetFailedAttempts(String email);

    @Transactional
    void lock(User user);

    UserAccessResponse updateAccess(UserAccessUpdateRequest updateRequest);
}
