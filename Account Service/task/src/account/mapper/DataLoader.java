package account.mapper;

import account.model.user.Group;
import account.service.group.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import static account.model.user.Role.*;

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
            groupService.save(new Group(ROLE_ADMINISTRATOR));
            groupService.save(new Group(ROLE_ACCOUNTANT));
            groupService.save(new Group(ROLE_USER));
            groupService.save(new Group(ROLE_AUDITOR));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
