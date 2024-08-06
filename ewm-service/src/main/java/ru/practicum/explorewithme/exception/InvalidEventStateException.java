package ru.practicum.explorewithme.exception;

/**
 * Exception thrown when an invalid event state is encountered.
 */
public class InvalidEventStateException extends RuntimeException {

    /**
     * Constructs a new InvalidEventStateException with the specified detail
     * message.
     *
     * @param message the detail message
     */
    public InvalidEventStateException(final String message) {
        super(message);
    }
}
