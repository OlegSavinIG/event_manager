package ru.practicum.explorewithme.event.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.StatisticResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class EventClient {
    private final RestTemplate restTemplate;

    @Value("${stats.server.url}")
    private String statsServerUrl;

    public void sendRequestData(String remoteAddr, String requestURI) {
        Map<String, String> requestData = new HashMap<>();
        requestData.put("remoteAddr", remoteAddr);
        requestData.put("requestURI", requestURI);
        restTemplate.postForObject(statsServerUrl + "/hit", requestData, Void.class);
    }

    @Async
    public CompletableFuture<Integer> getEventViews(Long eventId) {
        String url = String.format("%s/stats?start=%s&end=%s&uris=/events/%d",
                statsServerUrl,
                LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                eventId);
        List<StatisticResponse> response = List.of(restTemplate.getForObject(url, StatisticResponse[].class));
        return CompletableFuture.completedFuture(response.size());
    }
}
