package ru.practicum.explorewithme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for wrong time errors.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongTimeException extends RuntimeException {

    /**
     * Constructor for WrongTimeException.
     *
     * @param message the detail message
     */
    public WrongTimeException(final String message) {
        super(message);
    }
}
