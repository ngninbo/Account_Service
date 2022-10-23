package account.handler;

import account.domain.AccountServiceCustomErrorMessage;
import account.exception.*;
import account.exception.admin.AdminDeletionException;
import account.exception.admin.InvalidRoleException;
import account.exception.admin.RoleUpdateException;
import account.exception.admin.UserNotFoundException;
import account.exception.payment.PasswordUpdateException;
import account.exception.payment.PaymentNotFoundException;
import account.exception.payment.PaymentSavingException;
import account.exception.AccountStatusUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.LockedException;
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

    @ExceptionHandler({PasswordUpdateException.class, PaymentSavingException.class, PaymentNotFoundException.class,
            ConstraintViolationException.class, AdminDeletionException.class, RoleUpdateException.class, AccountStatusUpdateException.class, LockedException.class})
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

    @ExceptionHandler({UserNotFoundException.class, InvalidRoleException.class})
    public ResponseEntity<AccountServiceCustomErrorMessage> handleNotFound(Exception exception, HttpServletRequest request) {
        AccountServiceCustomErrorMessage body = AccountServiceCustomErrorMessage.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<AccountServiceCustomErrorMessage> handleAccessDenied(HttpServletRequest request) {
        AccountServiceCustomErrorMessage body = AccountServiceCustomErrorMessage.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message("Access Denied!")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}
