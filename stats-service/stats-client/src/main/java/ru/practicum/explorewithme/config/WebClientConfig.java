package ru.practicum.explorewithme.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class WebClientConfig {
//    @Value("${ewm.service.url}")
    private final String statsServerUrl = "http://localhost:9090";
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl(statsServerUrl)
                .filter((request, next) -> next.exchange(request)
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                        .onErrorResume(WebClientResponseException.class, e -> {
                            System.out.println("Error response code: " + e.getRawStatusCode() + ", body: " + e.getResponseBodyAsString());
                            return next.exchange(request);
                        })
                        .onErrorResume(throwable -> {
                            System.out.println("Unknown error: " + throwable.getMessage());
                            return next.exchange(request);
                        }));
    }
}

