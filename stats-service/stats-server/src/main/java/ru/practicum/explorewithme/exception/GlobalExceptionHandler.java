package ru.practicum.explorewithme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling specific exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles WrongTimeException.
     *
     * @param ex the WrongTimeException
     * @return the error message
     */
    @ExceptionHandler(WrongTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleWrongTimeException(final WrongTimeException ex) {
        return ex.getMessage();
    }
}
