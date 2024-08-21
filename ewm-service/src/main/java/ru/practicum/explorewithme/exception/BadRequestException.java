package ru.practicum.explorewithme.exception;

/**
 * Exception thrown when a bad request.
 */
public class BadRequestException extends RuntimeException {
    /**
     * Constructs a new AlreadyExistException with the specified detail message.
     *
     * @param message the detail message
     */
    public BadRequestException(String message) {
        super(message);
    }
}

