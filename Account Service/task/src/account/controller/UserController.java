package account.controller;

import account.domain.PasswordChangeResponse;
import account.domain.UserDeletionResponse;
import account.model.*;
import account.service.GroupService;
import account.util.exception.*;
import account.domain.UserDto;
import account.mapper.UserMapper;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final GroupService groupService;

    @Autowired
    public UserController(UserService userService, UserMapper mapper, PasswordEncoder encoder, GroupService groupService) {
        this.userService = userService;
        this.mapper = mapper;
        this.encoder = encoder;
        this.groupService = groupService;
    }

    @PostMapping(path = "/auth/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signup(@Valid @RequestBody User user) throws AccountServiceException {

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            throw new AccountServiceException(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        List<User> users = userService.findAll();
        var group = groupService.findByRole(users.isEmpty() ? Role.ROLE_ADMINISTRATOR.name() : Role.ROLE_USER.name());

        user.setEmail(user.getEmail().toLowerCase());
        user.getGroups().add(group.orElseThrow());
        user.setPassword(encoder.encode(user.getPassword()));
        return ResponseEntity.ok(mapper.toDto(userService.save(user)));
    }

    @PostMapping(path = "/auth/changepass", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PasswordChangeResponse> changePassword(@Valid @RequestBody PasswordChangeRequest request,
                                                                 @AuthenticationPrincipal UserDetails userDetails) throws PasswordUpdateException {

        if (encoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new PasswordUpdateException("The passwords must be different!");
        }

        var currentUser = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        currentUser.setPassword(encoder.encode(request.getPassword()));
        currentUser = userService.save(currentUser);
        return ResponseEntity.ok(PasswordChangeResponse.builder()
                .email(currentUser.getEmail().toLowerCase()).status(PasswordChangeResponse.DEFAULT_STATUS)
                .build());
    }

    @GetMapping(path = "/admin/user")
    public ResponseEntity<List<UserDto>> findAll() {
        final List<User> users = userService.findAll();
        return ResponseEntity.ok(users.isEmpty() ? List.of() : mapper.toList(users));
    }

    @DeleteMapping("/admin/user/{email}")
    public ResponseEntity<UserDeletionResponse> delete(@PathVariable String email, @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException, AdminDeletionException {

        final User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        if (user.isAdmin() && email.equals(userDetails.getUsername())) {
            throw new AdminDeletionException("Can't remove ADMINISTRATOR role!");
        }
        return ResponseEntity.ok(userService.deleteUserByEmail(email));
    }

    @PutMapping("/admin/user/role")
    public ResponseEntity<UserDto> updateRole(@Valid @RequestBody RoleUpdateRequest request) throws UserNotFoundException,
            RoleUpdateException, AdminDeletionException, InvalidRoleException {
        return ResponseEntity.ok(userService.updateRole(request));
    }
}
