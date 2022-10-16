package account.controller;

import account.domain.PasswordChangeResponse;
import account.util.exception.AccountServiceException;
import account.util.exception.PasswordUpdateException;
import account.domain.UserDto;
import account.mapper.UserMapper;
import account.model.PasswordChangeRequest;
import account.model.User;
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

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController(UserService userService, UserMapper mapper, PasswordEncoder encoder) {
        this.userService = userService;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @PostMapping(path = "/auth/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signup(@Valid @RequestBody User user) throws AccountServiceException {

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            throw new AccountServiceException(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
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
}
