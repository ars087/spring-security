package TestJavaCode.AuthApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {

    @ExceptionHandler(AccountBlockException.class)
    public ResponseEntity<?> handleAccountLockedException(AccountBlockException ex) {
        return ResponseEntity
            .status(HttpStatus.LOCKED)
            .body(new ExceptionResponse(HttpStatus.LOCKED.toString(), ex.getMessage()));
    }


    @ExceptionHandler(CustomExceptionNotFound.class)
    public ResponseEntity<?> userNotFoundException(CustomExceptionNotFound ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ExceptionResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage()));
    }


    @ExceptionHandler(CustomBadCredentialsExceptions.class)
    public ResponseEntity<?> userNotFoundException(CustomBadCredentialsExceptions ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.toString(), ex.getMessage()));
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<?> refreshTokenNotFoundException(TokenRefreshException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ExceptionResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage()));
    }

}
