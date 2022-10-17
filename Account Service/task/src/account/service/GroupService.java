package account.service;

import account.model.Group;
import account.model.Role;
import account.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class GroupService implements IGroupService {

    private final GroupRepository repository;

    @Autowired
    public GroupService(GroupRepository repository) {
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
        if (tmp.isPresent()) {
            group.setId(tmp.get().getId());
            return this.repository.save(group);
        }
        return repository.save(group);
    }
}
