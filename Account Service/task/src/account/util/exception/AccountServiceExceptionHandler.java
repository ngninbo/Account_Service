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
        AccountServiceCustomErrorMessage body = new AccountServiceCustomErrorMessage(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<AccountServiceCustomErrorMessage> handleValidationError(MethodArgumentNotValidException e,
                                                                                  HttpServletRequest request) {
        AccountServiceCustomErrorMessage body = new AccountServiceCustomErrorMessage(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            PasswordUpdateException.class,
            PaymentSavingException.class,
            PaymentNotFoundException.class,
            ConstraintViolationException.class})
    public ResponseEntity<AccountServiceCustomErrorMessage> handleChangeException(Exception exception,
                                                                                  HttpServletRequest request){
        AccountServiceCustomErrorMessage body = new AccountServiceCustomErrorMessage(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
