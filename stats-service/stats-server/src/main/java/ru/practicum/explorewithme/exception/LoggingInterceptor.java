package ru.practicum.explorewithme.exception;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Interceptor for logging HTTP requests and responses.
 */
public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    /**
     * Logs the details of the HTTP request.
     */
    @Override
    public ClientHttpResponse intercept(
            final HttpRequest request,
            final byte[] body,
            final ClientHttpRequestExecution execution)
            throws IOException {
        logRequestDetails(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponseDetails(response);
        return response;
    }

    /**
     * Logs the details of the HTTP request.
     *
     * @param request the HTTP request
     * @param body    the body of the request
     * @throws IOException if an I/O error occurs
     */
    private void logRequestDetails(
            final HttpRequest request, final byte[] body)
            throws IOException {
        System.out.println("URI: " + request.getURI());
        System.out.println("Method: " + request.getMethod());
        System.out.println("Headers: " + request.getHeaders());
        System.out.println(
                "Request body: " + new String(body, StandardCharsets.UTF_8));
    }

    /**
     * Logs the details of the HTTP response.
     *
     * @param response the HTTP response
     * @throws IOException if an I/O error occurs
     */
    private void logResponseDetails(
            final ClientHttpResponse response) throws IOException {
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Status text: " + response.getStatusText());
        System.out.println("Headers: " + response.getHeaders());
    }
}
