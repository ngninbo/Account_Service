package account.service.group;

import account.model.user.Group;
import account.model.user.Role;
import account.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repository;

    @Autowired
    public GroupServiceImpl(GroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Group> findByRole(String role) {
        return this.repository.findByRole(Role.valueOf(role));
    }

    @Override
    @Transactional
    public Group save(Group group) {
        var tmp = this.repository.findByRole(group.getRole());
        tmp.ifPresent(value -> group.setId(value.getId()));
        return repository.save(group);
    }
}
