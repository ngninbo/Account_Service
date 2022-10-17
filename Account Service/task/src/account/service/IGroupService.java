package account.service;

import account.model.Group;

import java.util.Optional;

public interface IGroupService {

    Optional<Group> findByRole(String role);
    Group save(Group group);
}
