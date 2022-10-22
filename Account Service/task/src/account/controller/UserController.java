package account.controller;

import account.domain.user.PasswordChangeResponse;
import account.domain.user.UserDeletionResponse;
import account.exception.admin.AdminDeletionException;
import account.exception.admin.InvalidRoleException;
import account.exception.admin.RoleUpdateException;
import account.exception.admin.UserNotFoundException;
import account.exception.payment.PasswordUpdateException;
import account.model.event.Event;
import account.model.user.*;
import account.exception.*;
import account.domain.user.UserDto;
import account.mapper.UserMapper;
import account.service.event.EventService;
import account.service.group.GroupService;
import account.service.user.UserService;
import account.util.LogEvent;
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

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final GroupService groupService;
    private final EventService eventService;
    private final PasswordEncoder encoder;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public UserController(UserService userService, UserMapper mapper, PasswordEncoder encoder, GroupService groupService, EventService eventService, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.mapper = mapper;
        this.encoder = encoder;
        this.groupService = groupService;
        this.eventService = eventService;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping(path = "/auth/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signup(@Valid @RequestBody User user, @AuthenticationPrincipal UserDetails userDetails) throws AccountServiceException {

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            throw new AccountServiceException(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        List<User> users = userService.findAll();
        var group = groupService.findByRole(users.isEmpty() ? Role.ROLE_ADMINISTRATOR.name() : Role.ROLE_USER.name());

        user.setEmail(user.getEmail().toLowerCase());
        user.getGroups().add(group.orElseThrow());
        user.setPassword(encoder.encode(user.getPassword()));
        final User savedUser = userService.save(user);
        var event = new Event(LogEvent.CREATE_USER, userDetails != null ? userDetails.getUsername() : "Anonymous",
                user.getEmail(), httpServletRequest.getRequestURI());
        eventService.save(event);
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

        var event = new Event(LogEvent.CHANGE_PASSWORD, username, username, httpServletRequest.getRequestURI());
        eventService.save(event);
        return ResponseEntity.ok(PasswordChangeResponse.builder()
                .email(currentUser.getEmail().toLowerCase()).status(PasswordChangeResponse.DEFAULT_STATUS)
                .build());
    }
}
