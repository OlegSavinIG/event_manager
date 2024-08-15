package ru.practicum.explorewithme.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.StatisticRequest;
import ru.practicum.explorewithme.StatisticResponse;
import ru.practicum.explorewithme.client.StatisticClient;
import ru.practicum.explorewithme.model.StatisticEntity;
import ru.practicum.explorewithme.model.StatisticMapper;
import ru.practicum.explorewithme.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link StatisticService} interface.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StatisticServiceImpl implements StatisticService {
    /**
     * Repository.
     */
    private final StatisticRepository repository;
    /**
     * Client.
     */
    private final StatisticClient client;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveStatistic(final StatisticRequest request) {
        log.info("Attempting to save statistic for URI: {}", request.getUri());
        boolean exist = repository.existsByIpAndUri(request.getIp(), request.getUri());
        if (exist) {
            log.info("Statistic already saved for uri {} and ip {}",
                    request.getUri(), request.getIp());
            return;
        }
        StatisticEntity newEntity = StatisticMapper.toEntity(request);
        newEntity.setCreationTime(LocalDateTime.now());
        repository.save(newEntity);
        log.info("Statistic saved successfully for URI: {}", request.getUri());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<StatisticResponse> getStatistic(final LocalDateTime start,
                                                final LocalDateTime end,
                                                final List<String> uris,
                                                final boolean unique) {
        log.info("Fetching statistics for uris {} from {} to {}, unique: {}",
                uris, start, end, unique);

        List<StatisticResponse> statistics;

        if (unique) {
            statistics = (uris != null && !uris.isEmpty())
                    ? repository
                    .findStatisticsWithUniqueIpAndUriIn(uris, start, end)
                    : repository
                    .findStatisticsWithUniqueIp(start, end);
            log.info("Fetched {} unique statistics records",
                    statistics.size());
        } else {
            statistics = (uris != null && !uris.isEmpty())
                    ? repository.findStatisticByUriIn(uris, start, end)
                    : repository.findAllByCreationTimeBetween(start, end);
            log.info("Fetched {} statistics records", statistics.size());
        }
        return statistics;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, Long> getEventViews(final List<String> uris) {
        log.info("Fetching statistics records by uris: {}", uris);
        List<StatisticResponse> stats = repository.findStatisticByUriIn(uris);
        log.info("Stats found: {}", stats);

        Map<Long, Long> eventsViews = new HashMap<>();
        stats.forEach(statistic -> {
            String uri = statistic.getUri();
            Long eventId = eventIdExtractor(uri);
            eventsViews.put(eventId, statistic.getHits());
        });

        log.info("Views found: {}", eventsViews);
        return eventsViews;
    }

    /**
     * Event extractor.
     * @param uri event uris
     * @return Long id
     */
    private Long eventIdExtractor(final String uri) {
        String[] parts = uri.split("/");
        String idString = parts[parts.length - 1];
        try {
            return Long.parseLong(idString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID in URI: " + uri, e);
        }
    }
}
