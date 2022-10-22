package account.service.user;

import account.domain.user.PasswordChangeResponse;
import account.domain.user.UserAccessResponse;
import account.domain.user.UserDeletionResponse;
import account.domain.user.UserDto;
import account.exception.AccountServiceException;
import account.exception.payment.PasswordUpdateException;
import account.model.user.*;
import account.exception.admin.AdminDeletionException;
import account.exception.admin.InvalidRoleException;
import account.exception.admin.RoleUpdateException;
import account.exception.admin.UserNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    User save(User user);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    UserDeletionResponse deleteUserByEmail(String email) throws UserNotFoundException, AdminDeletionException;

    UserDto updateRole(RoleUpdateRequest request) throws UserNotFoundException, InvalidRoleException, RoleUpdateException, AdminDeletionException;

    UserDto grant(User user, Group group) throws RoleUpdateException;

    UserDto revoke(User user, Group group) throws RoleUpdateException, AdminDeletionException;

    User increaseFailedAttempts(User user);

    @Transactional
    void resetFailedAttempts(String email);

    @Transactional
    void lock(User user);

    UserAccessResponse updateAccess(UserAccessUpdateRequest updateRequest) throws UserNotFoundException, AdminDeletionException, RoleUpdateException;
}
