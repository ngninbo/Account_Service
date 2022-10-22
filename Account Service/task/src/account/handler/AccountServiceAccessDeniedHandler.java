package account.handler;

import account.model.event.Event;
import account.service.event.EventService;
import account.util.LogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccountServiceAccessDeniedHandler implements AccessDeniedHandler {
    private final EventService eventService;


    @Autowired
    public AccountServiceAccessDeniedHandler(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            var userDetails = (UserDetails) auth.getPrincipal();
            var event = new Event(LogEvent.ACCESS_DENIED, userDetails.getUsername(), request.getRequestURI(), request.getRequestURI());
            eventService.save(event);
            response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied!");
        }
    }
}
