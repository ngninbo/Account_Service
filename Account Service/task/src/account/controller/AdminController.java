package account.controller;

import account.domain.user.UserAccessResponse;
import account.domain.user.UserDeletionResponse;
import account.domain.user.UserDto;
import account.exception.admin.AdminDeletionException;
import account.exception.admin.UserNotFoundException;
import account.mapper.UserMapper;
import account.model.event.EventBuilder;
import account.model.user.RoleUpdateRequest;
import account.model.user.User;
import account.model.user.UserAccessUpdateRequest;
import account.service.event.EventService;
import account.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static account.util.LogEvent.*;

@RestController
@RequestMapping(path = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(name = "Admin service")
public class AdminController {

    private final UserService userService;
    private final UserMapper mapper;
    private final EventService eventService;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public AdminController(UserService userService,
                           UserMapper mapper,
                           EventService eventService,
                           HttpServletRequest httpServletRequest) {
        this.userService = userService;
        this.mapper = mapper;
        this.httpServletRequest = httpServletRequest;
        this.eventService = eventService;
    }


    @GetMapping(path = "/user")
    @Operation(summary = "Get list of all users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) })})
    public ResponseEntity<List<UserDto>> findAll() {
        final List<User> users = userService.findAll();
        return ResponseEntity.ok(mapper.toList(users));
    }

    @DeleteMapping("/user/{email}")
    @Operation(summary = "Delete user by mail")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Deleted successfully!",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDeletionResponse.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Can't remove ADMINISTRATOR role!",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User by given name not found | User not found!", content = @Content)})
    public ResponseEntity<UserDeletionResponse> delete(@PathVariable String email,
                                                       @AuthenticationPrincipal UserDetails userDetails) {

        final User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(String.format("User by name = %s not found", userDetails.getUsername())));
        if (user.isAdmin() && email.equals(userDetails.getUsername())) {
            throw new AdminDeletionException("Can't remove ADMINISTRATOR role!");
        }
        final UserDeletionResponse response = userService.deleteUserByEmail(email);

        EventBuilder eb = EventBuilder.init()
                .withAction(DELETE_USER)
                .withSubject(userDetails.getUsername())
                .withObject(email)
                .withPath(httpServletRequest.getRequestURI());

        eventService.save(eb.build());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/role")
    @Operation(summary = "Grant or remove user role")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "The user cannot combine administrative and business roles! | Can't remove ADMINISTRATOR role! The user does not have a role! The user must have at least one role!",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Unsupported operation! Role not found! | User not found!", content = @Content)})
    public ResponseEntity<UserDto> updateRole(@Valid @RequestBody RoleUpdateRequest request,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        final UserDto userDto = userService.updateRole(request);
        EventBuilder eb = EventBuilder.init()
                .withSubject(userDetails.getUsername())
                .withPath(httpServletRequest.getRequestURI());
        if ("GRANT".equals(request.getOperation())) {
            eb
                    .withAction(GRANT_ROLE)
                    .withObject(String.format("Grant role %s to %s", request.getRole(), request.getEmail().toLowerCase()));
        } else {
            eb
                    .withAction(REMOVE_ROLE)
                    .withObject(String.format("Remove role %s from %s", request.getRole(), request.getEmail().toLowerCase()));
        }

        eventService.save(eb.build());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/user/access")
    @Operation(summary = "Lock or unlock user access")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserAccessResponse.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Can't lock the ADMINISTRATOR! | Unsupported operation!",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found!", content = @Content)})
    public ResponseEntity<UserAccessResponse> updateAccess(@Valid @RequestBody UserAccessUpdateRequest updateRequest,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        var response = userService.updateAccess(updateRequest);
        EventBuilder eb = EventBuilder.init()
                .withSubject(userDetails.getUsername())
                .withPath(httpServletRequest.getRequestURI());
        if ("LOCK".equals(updateRequest.getOperation())) {
            eb
                    .withAction(LOCK_USER)
                    .withObject(String.format("Lock user %s", updateRequest.getEmail().toLowerCase()));
        } else {
            eb
                    .withAction(UNLOCK_USER)
                    .withObject(String.format("Unlock user %s", updateRequest.getEmail().toLowerCase()));
        }

        eventService.save(eb.build());
        return ResponseEntity.ok(response);
    }
}
