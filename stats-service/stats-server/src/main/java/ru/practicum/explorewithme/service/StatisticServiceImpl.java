package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.StatisticRequest;
import ru.practicum.explorewithme.StatisticResponse;
import ru.practicum.explorewithme.client.StatisticClient;
import ru.practicum.explorewithme.model.StatisticEntity;
import ru.practicum.explorewithme.model.StatisticMapper;
import ru.practicum.explorewithme.repository.StatisticRepository;

import javax.transaction.Transactional;
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
                    ? repository.
                    findStatisticsWithUniqueIpAndUriIn(uris, start, end)
                    : repository.
                    findStatisticsWithUniqueIp(start, end);
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

    @Override
    public Map<Long, Long> getEventViews(List<String> uris) {
        List<StatisticResponse> stats =
                repository.findStatisticByUriIn(uris);
        Map<Long, Long> eventsViews = new HashMap<>();
        stats.stream()
                .peek(statistic -> {
                    String uri = statistic.getUri();
                    eventsViews.put(eventIdExtractor(uri),
                            statistic.getHits());
                });
        return eventsViews;
    }

    private Long eventIdExtractor(String uri) {
        char lastChar = uri.charAt(uri.length() - 1);
        if (!Character.isDigit(lastChar)) {
            throw new IllegalStateException(
                    "Error in forming event id from uri " + uri);
        }
        String lastCharAsString = Character.toString(lastChar);
        return Long.parseLong(lastCharAsString);
    }
}
