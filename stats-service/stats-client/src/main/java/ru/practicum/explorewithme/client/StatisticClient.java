package ru.practicum.explorewithme.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.explorewithme.StatisticRequest;

import java.util.List;
import java.util.Map;

/**
 * Client class for interacting with the statistics service.
 * Provides methods to send statistics and retrieve event views.
 */
@Component
@Slf4j
public class StatisticClient {

    /**
     * WebClient instance used for making HTTP requests.
     */
    private final WebClient webClient;

    /**
     * Constructs a StatisticClient with the provided WebClient builder.
     *
     * @param webClientBuilder a WebClient.Builder used to build the
     *                         WebClient instance.
     */
    public StatisticClient(final WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Sends statistical data to the statistics service.
     *
     * @param stats the StatisticRequest object containing the data to
     *              be sent.
     * @return a Mono<Void> indicating completion or error.
     */
    public Mono<Void> sendStats(final StatisticRequest stats) {
        return webClient.post()
                .uri("/hit")
                .bodyValue(stats)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.info("Error sending stats: {}", clientResponse
                            .statusCode());
                    return Mono.error(new RuntimeException("Failed to send "
                            + "stats"));
                })
                .bodyToMono(Void.class);
    }

    /**
     * Retrieves event views from the statistics service based on
     * provided URIs.
     *
     * @param uris a list of URI strings for which to retrieve event
     *             views.
     * @return a Mono<Map<Long, Integer>> containing event IDs and
     * their view counts.
     */
    public Mono<Map<Long, Integer>> getEventViews(final List<String> uris) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/views")
                        .queryParam("uris", String.join(",", uris))
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}
