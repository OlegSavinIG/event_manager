package ru.practicum.explorewithme.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.StatisticResponse;

import java.util.List;

/**
 * Service for handling statistics via RestTemplate.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticClient {

    private final RestTemplate restTemplate;

    public void sendStatistics(List<StatisticResponse> statistics) {
        try {
            restTemplate.postForObject("/external-service-endpoint", statistics, Void.class);
            log.info("Successfully sent statistics to external service");
        } catch (Exception e) {
            log.error("Failed to send statistics to external service", e);
        }
    }
}
