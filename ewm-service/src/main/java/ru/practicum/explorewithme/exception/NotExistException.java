package ru.practicum.explorewithme.exception;

/**
 * Exception thrown when a resource does not exist.
 */
public class NotExistException extends RuntimeException {

    /**
     * Constructs a new NotExistException with the specified detail message.
     *
     * @param message the detail message
     */
    public NotExistException(String message) {
        super(message);
    }
}
