package account.mapper;

import account.model.Group;
import account.model.Role;
import account.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final GroupService groupService;

    @Autowired
    public DataLoader(GroupService groupService) {
        this.groupService = groupService;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createRoles();
    }

    private void createRoles() {
        try {
            groupService.save(new Group(Role.ROLE_ADMINISTRATOR));
            groupService.save(new Group(Role.ROLE_ACCOUNTANT));
            groupService.save(new Group(Role.ROLE_USER));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
