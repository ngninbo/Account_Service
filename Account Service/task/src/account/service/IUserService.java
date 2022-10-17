package account.service;

import account.domain.UserDeletionResponse;
import account.domain.UserDto;
import account.model.Group;
import account.model.Role;
import account.model.RoleUpdateRequest;
import account.model.User;
import account.util.exception.AdminDeletionException;
import account.util.exception.InvalidRoleException;
import account.util.exception.RoleUpdateException;
import account.util.exception.UserNotFoundException;

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
}
