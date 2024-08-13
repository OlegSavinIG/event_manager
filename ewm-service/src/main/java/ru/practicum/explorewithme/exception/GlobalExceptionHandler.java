package ru.practicum.explorewithme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for handling various exceptions in the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions.
     *
     * @param ex the MethodArgumentNotValidException
     * @return a response entity with error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidationExceptions(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles general exceptions.
     *
     * @param ex the Exception
     * @return a response entity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleExceptions(
            final Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "BAD_REQUEST");
        errorResponse.put("reason", "Incorrectly made request.");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles constraint violation exceptions.
     *
     * @param ex      the ConstraintViolationException
     * @param request the WebRequest
     * @return a response entity with error details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            final ConstraintViolationException ex,
            final WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.CONFLICT);
        body.put("reason", "Integrity constraint has been violated.");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Handles not exist exceptions.
     *
     * @param ex      the NotExistException
     * @param request the WebRequest
     * @return a response entity with error details
     */
    @ExceptionHandler(NotExistException.class)
    public ResponseEntity<Object> handleNotExistException(
            final NotExistException ex,
            final WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND);
        body.put("reason", "Resource not found.");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles already exist exceptions.
     *
     * @param ex      the AlreadyExistException
     * @param request the WebRequest
     * @return a response entity with error details
     */
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Object> handleAlreadyExistException(
            final AlreadyExistException ex,
            final WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.CONFLICT);
        body.put("reason", "The resource already exists.");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
    /**
     * Handles already exist exceptions.
     *
     * @param ex      the ConflictException
     * @param request the WebRequest
     * @return a response entity with error details
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictException(
            final AlreadyExistException ex,
            final WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.CONFLICT);
        body.put("reason", "Conflict in arguments.");
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
