package ru.practicum.explorewithme.exception;

/**
 * Custom exception for generic errors.
 */
public class CustomGenericException extends RuntimeException {

    /**
     * Constructor for CustomGenericException.
     *
     * @param message the detail message
     */
    public CustomGenericException(final String message) {
        super(message);
    }
}
