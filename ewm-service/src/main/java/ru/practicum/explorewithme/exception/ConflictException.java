package ru.practicum.explorewithme.exception;
/**
 * Exception thrown when a resource already exists.
 */
public class ConflictException extends RuntimeException {
    /**
     * Constructs a new AlreadyExistException with the specified detail message.
     *
     * @param s the detail message
     */
    public ConflictException(final String s) {
        super(s);
    }
}
