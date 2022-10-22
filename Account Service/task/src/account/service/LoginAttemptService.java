package account.service;

import account.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;
    private UserService userService;


}
