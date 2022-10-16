package account.util.exception;

import account.domain.AccountServiceCustomErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class AccountServiceExceptionHandler {

    @ExceptionHandler({AccountServiceException.class})
    public ResponseEntity<AccountServiceCustomErrorMessage> handleBadRequest(Exception e, HttpServletRequest request) {
        AccountServiceCustomErrorMessage body = AccountServiceCustomErrorMessage.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(e.getMessage())
                .message(AccountServiceCustomErrorMessage.DEFAULT_MESSAGE)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<AccountServiceCustomErrorMessage> handleValidationError(MethodArgumentNotValidException e,
                                                                                  HttpServletRequest request) {
        AccountServiceCustomErrorMessage body = AccountServiceCustomErrorMessage.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            PasswordUpdateException.class,
            PaymentSavingException.class,
            PaymentNotFoundException.class,
            ConstraintViolationException.class})
    public ResponseEntity<AccountServiceCustomErrorMessage> handleChangeException(Exception exception,
                                                                                  HttpServletRequest request){
        AccountServiceCustomErrorMessage body = AccountServiceCustomErrorMessage.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
