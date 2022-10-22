package account.service.group;

import account.model.user.Group;

import java.util.Optional;

public interface IGroupService {

    Optional<Group> findByRole(String role);
    Group save(Group group);
}
