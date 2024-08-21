package ru.practicum.explorewithme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Configuration class for setting up WebClient with custom settings.
 * This class provides a configured WebClient.Builder bean that is
 * used to create WebClient instances with base URL, retry logic,
 * and error handling.
 */
@Configuration
public class WebClientConfig {

    /**
     * The base URL of the statistics server.
     */
    private final String statsServerUrl = "http://stats-server:8080";
    /**
     * Max attempts.
     */
    private final int maxAttempts = 3;
    /**
     * Creates a WebClient.Builder bean with configured base URL,
     * retry logic, and error handling.
     *
     * @return a WebClient.Builder instance with custom configuration.
     */

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl(statsServerUrl)
                .filter((request, next) -> next.exchange(request)
                        .retryWhen(Retry.backoff(
                                maxAttempts, Duration.ofSeconds(2)))
                        .onErrorResume(WebClientResponseException.class, e -> {
                            System.out.println("Error response code: " + e
                                    .getRawStatusCode() + ", body: " + e
                                    .getResponseBodyAsString());
                            return next.exchange(request);
                        })
                        .onErrorResume(throwable -> {
                            System.out.println("Unknown error: " + throwable
                                    .getMessage());
                            return next.exchange(request);
                        }));
    }
}
