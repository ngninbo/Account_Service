package account.mapper;

import account.model.user.Group;
import account.model.user.Role;
import account.service.group.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final GroupServiceImpl groupService;

    @Autowired
    public DataLoader(GroupServiceImpl groupService) {
        this.groupService = groupService;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createRoles();
    }

    private void createRoles() {
        try {
            Arrays.stream(Role.values())
                    .map(Group::new)
                    .forEach(groupService::save);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
