package account.config;

import account.model.user.User;
import account.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserService userService;

    @Autowired
    public AuthenticationSuccessEventListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        var userDetails = (UserDetails) event.getAuthentication().getPrincipal();

        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        if (user.getFailedAttempt() > 0) {
            userService.resetFailedAttempts(user.getEmail());
        }
    }
}
