package ru.practicum.explorewithme.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.explorewithme.StatisticRequest;
import ru.practicum.explorewithme.StatisticResponse;

import java.util.List;
import java.util.Map;

@Component
public class StatisticClient {

    private final WebClient webClient;
    public StatisticClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    public Mono<Void> sendStats(StatisticRequest stats) {
        return webClient.post()
                .uri("/hit")
                .bodyValue(stats)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Map<Long, Integer>> getEventViews(List<String> uris) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/views")
                        .queryParam("uris", String.join(",", uris))
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}

