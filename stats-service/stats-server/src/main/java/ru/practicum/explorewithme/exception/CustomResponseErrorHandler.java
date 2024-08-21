package ru.practicum.explorewithme.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Custom error handler for REST responses.
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {
    /**
     * Handles the error from the response.
     */
    @Override
    public boolean hasError(
            final ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    /**
     * Handles the error from the response.
     *
     * @param response the client HTTP response
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handleError(
            final ClientHttpResponse response) throws IOException {
        System.out.println("Error status code: " + response.getStatusCode());
        System.out.println("Error status text: " + response.getStatusText());

        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode.equals(BAD_REQUEST)) {
            throw new CustomBadRequestException(
                    "Bad request: " + response.getStatusText());
        } else if (statusCode.equals(NOT_FOUND)) {
            throw new CustomNotFoundException(
                    "Not found: " + response.getStatusText());
        }
        throw new CustomGenericException(
                "Generic error: " + response.getStatusText());
    }
}
