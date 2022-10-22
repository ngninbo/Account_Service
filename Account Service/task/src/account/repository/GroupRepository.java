package account.repository;

import account.model.user.Group;
import account.model.user.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<Group, Long> {

    Optional<Group> findByRole(Role role);
}
