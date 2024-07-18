package ru.practicum.explorewithme.exception;

/**
 * Exception thrown when a resource already exists.
 */
public class AlreadyExistException extends RuntimeException {

    /**
     * Constructs a new AlreadyExistException with the specified detail message.
     *
     * @param message the detail message
     */
    public AlreadyExistException(String message) {
        super(message);
    }
}
