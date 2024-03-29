package account.controller;

import account.domain.AccountServiceCustomErrorMessage;
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
import account.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static account.model.user.Role.ADMINISTRATOR;
import static account.model.user.Role.USER;
import static account.util.LogEvent.*;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(name = "Auth service", description = "User access management")
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final GroupServiceImpl groupService;
    private final EventServiceImpl eventService;
    private final PasswordEncoder encoder;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public UserController(UserService userService, UserMapper mapper, PasswordEncoder encoder, GroupServiceImpl groupService, EventServiceImpl eventService, HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.mapper = mapper;
        this.encoder = encoder;
        this.groupService = groupService;
        this.eventService = eventService;
        this.httpServletRequest = httpServletRequest;
    }

    @PostMapping(path = "/auth/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400",
                    description = "User exists! | Email must not be empty | " +
                    "Email from given domain not allowed | The user name must not be empty | " +
                    "The password is in the hacker's database! The password length must be at least 12 chars!",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) }),
            @ApiResponse(responseCode = "404", description = "Group not found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) })
    })
    public ResponseEntity<UserDto> signup(@Valid @RequestBody User user, @AuthenticationPrincipal UserDetails userDetails) {

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            throw new AccountServiceException(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        List<User> users = userService.findAll();
        var group = groupService.findByRole(users.isEmpty() ? ADMINISTRATOR.name() : USER.name());

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
    @Operation(description = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "The passwords must be different!"),
            @ApiResponse(responseCode = "401",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountServiceCustomErrorMessage.class)) })
    })
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
