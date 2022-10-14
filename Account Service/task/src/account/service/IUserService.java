package account.service;

import account.model.User;

import java.util.Optional;

public interface IUserService {

    Optional<User> findById(long userId);
    User save(User user);

    Optional<User> findByEmail(String email);
}
