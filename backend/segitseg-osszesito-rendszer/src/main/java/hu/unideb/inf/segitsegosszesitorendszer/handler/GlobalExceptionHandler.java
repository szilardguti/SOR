package hu.unideb.inf.segitsegosszesitorendszer.handler;

import hu.unideb.inf.segitsegosszesitorendszer.enums.ErrorCodes;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.JwtNotFoundException;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.NewJwtRequiredException;
import hu.unideb.inf.segitsegosszesitorendszer.response.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.builder()
                        .exceptionMessage(ex.getMessage())
                .build());
    }

    @ExceptionHandler(JwtNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleJwtNotFoundException(JwtNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.builder()
                .exceptionMessage(ex.getMessage())
                .build());
    }

    @ExceptionHandler(NewJwtRequiredException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwtException(NewJwtRequiredException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .exceptionMessage("Kérjük jelentkezzen be újra!")
                .code(ErrorCodes.NEW_JWT.ordinal())
                .build());
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwtServletException(ServletException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .exceptionMessage(ex.getMessage())
                .code(ErrorCodes.NEW_JWT.ordinal())
                .build());
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.builder()
                .exceptionMessage(ex.getMessage())
                .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        String errorResponse = "Hibás felhasználónév vagy jelszó. Kérjük, próbálja meg újra!";
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder()
                .exceptionMessage(errorResponse)
                .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        String errorResponse = "Hozzáférés megtagadva. Nincs jogosultságod az erőforrás eléréséhez.";
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.builder()
                .exceptionMessage(errorResponse)
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> resolveException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Exception e,
                                              Integer code) {
        response.setStatus(410);
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .exceptionMessage(e.getMessage())
                .code(code)
                .build());
    }
}
