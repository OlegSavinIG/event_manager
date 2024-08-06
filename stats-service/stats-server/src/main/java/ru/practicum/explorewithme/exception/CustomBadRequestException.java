package ru.practicum.explorewithme.exception;

/**
 * Custom exception for bad requests.
 */
public class CustomBadRequestException extends RuntimeException {

    /**
     * Constructor for CustomBadRequestException.
     *
     * @param message the detail message
     */
    public CustomBadRequestException(final String message) {
        super(message);
    }
}
