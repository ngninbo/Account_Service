package account.config;

import account.model.event.EventBuilder;
import account.model.user.User;
import account.service.event.EventService;
import account.service.user.UserService;
import account.util.LogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final HttpServletRequest request;
    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public AuthenticationFailureEventListener(HttpServletRequest request, UserService userService, EventService eventService) {
        this.request = request;
        this.userService = userService;
        this.eventService = eventService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        String username = (String) event.getAuthentication().getPrincipal();
        var user = userService.findByEmail(username);

        EventBuilder eb = EventBuilder.init()
                .withAction(LogEvent.LOGIN_FAILED)
                .withSubject(username.toLowerCase())
                .withObject(request.getRequestURI())
                .withPath(request.getRequestURI());

        if (user.isEmpty()) {
            eventService.save(eb.build());
        } else {
            User currentUser = user.get();

            if (currentUser.isAdmin()) {
                return;
            }

            if (currentUser.isEnabled() && currentUser.isAccountNonLocked()) {
                currentUser = userService.increaseFailedAttempts(currentUser);
                eventService.save(eb.build());
            }

            if (currentUser.getFailedAttempt() >= UserService.MAX_FAILED_ATTEMPTS) {
                eb.withAction(LogEvent.BRUTE_FORCE);
                eventService.save(eb.build());

                userService.lock(currentUser);
                eb.withAction(LogEvent.LOCK_USER);
                eb.withObject(String.format("Lock user %s", username.toLowerCase()));
                eventService.save(eb.build());
            }
        }
    }
}
