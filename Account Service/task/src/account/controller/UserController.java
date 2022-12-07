package account.controller;

import account.domain.user.PasswordChangeResponse;
import account.domain.user.PasswordChangeResponseBuilder;
import account.exception.payment.PasswordUpdateException;
import account.model.event.EventBuilder;
import account.model.user.*;
import account.exception.*;
import account.domain.user.UserDto;
import account.mapper.UserMapper;
import account.service.event.EventServiceImpl;
import account.service.group.GroupServiceImpl;
import account.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static account.model.user.Role.ROLE_ADMINISTRATOR;
import static account.model.user.Role.ROLE_USER;
import static account.util.LogEvent.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UserController {

    private final UserServiceImpl userService;
    private final UserMapper mapper;
    private final GroupServiceImpl groupService;
    private final EventServiceImpl eventService;
    private final PasswordEncoder encoder;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public UserController(UserServiceImpl userService, UserMapper mapper, PasswordEncoder encoder, GroupServiceImpl groupService, EventServiceImpl eventService, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.mapper = mapper;
        this.encoder = encoder;
        this.groupService = groupService;
        this.eventService = eventService;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping(path = "/auth/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signup(@Valid @RequestBody User user, @AuthenticationPrincipal UserDetails userDetails) {

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            throw new AccountServiceException(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        List<User> users = userService.findAll();
        var group = groupService.findByRole(users.isEmpty() ? ROLE_ADMINISTRATOR.name() : ROLE_USER.name());

        user.setEmail(user.getEmail().toLowerCase());
        user.getGroups().add(group.orElseThrow());
        user.setPassword(encoder.encode(user.getPassword()));
        final User savedUser = userService.save(user);
        EventBuilder eb = EventBuilder.init()
                .withAction(CREATE_USER)
                .withPath(httpServletRequest.getRequestURI())
                .withSubject(userDetails != null ? userDetails.getUsername() : "Anonymous")
                .withObject(user.getEmail());
        eventService.save(eb.build());
        return ResponseEntity.ok(mapper.toDto(savedUser));
    }

    @PostMapping(path = "/auth/changepass", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordChangeResponse> changePassword(@Valid @RequestBody PasswordChangeRequest request,
                                                                 @AuthenticationPrincipal UserDetails userDetails) throws PasswordUpdateException {

        if (encoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new PasswordUpdateException("The passwords must be different!");
        }

        final String username = userDetails.getUsername();
        var currentUser = userService.findByEmail(username).orElseThrow();
        currentUser.setPassword(encoder.encode(request.getPassword()));
        currentUser = userService.save(currentUser);

        EventBuilder eb = EventBuilder.init()
                .withAction(CHANGE_PASSWORD)
                .withSubject(username)
                .withObject(username)
                .withPath(httpServletRequest.getRequestURI());

        eventService.save(eb.build());
        return ResponseEntity.ok(PasswordChangeResponseBuilder.init().withEmail(currentUser.getEmail().toLowerCase()).build());
    }
}
