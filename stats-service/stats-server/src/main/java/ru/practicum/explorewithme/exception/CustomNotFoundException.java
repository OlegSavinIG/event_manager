package ru.practicum.explorewithme.exception;

/**
 * Custom exception for not found errors.
 */
public class CustomNotFoundException extends RuntimeException {

    /**
     * Constructor for CustomNotFoundException.
     *
     * @param message the detail message
     */
    public CustomNotFoundException(final String message) {
        super(message);
    }
}
