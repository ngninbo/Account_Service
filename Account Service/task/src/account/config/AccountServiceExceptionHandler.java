package account.config;

import account.domain.AccountServiceCustomErrorMessage;
import account.exception.UserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class AccountServiceExceptionHandler {

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<AccountServiceCustomErrorMessage> handleBadRequest(UserExistException e, HttpServletRequest request) {
        AccountServiceCustomErrorMessage body = new AccountServiceCustomErrorMessage(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
