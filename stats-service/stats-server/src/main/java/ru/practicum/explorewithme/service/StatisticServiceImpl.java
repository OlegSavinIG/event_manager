package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.StatisticRequest;
import ru.practicum.explorewithme.StatisticResponse;
import ru.practicum.explorewithme.client.StatisticClient;
import ru.practicum.explorewithme.model.StatisticEntity;
import ru.practicum.explorewithme.model.StatisticMapper;
import ru.practicum.explorewithme.repository.StatisticRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        repository.save(StatisticMapper.toEntity(request));
        log.info("Statistic saved successfully for URI: {}", request.getUri());
//        if (request == null || request.getUri() == null) {
//            log.info("Request or URI is null. Cannot process saveStatistic.");
//            throw new IllegalArgumentException("Request and URI must not be null");
//        }
//
//        log.info("Attempting to save statistic for URI: {}", request.getUri());
//        StatisticEntity entity = repository.getByUri(request.getUri())
//                .orElseGet(() -> {
//                    log.info("No existing statistic found for URI: {}. Creating new record.", request.getUri());
//                    StatisticEntity newEntity = StatisticMapper.toEntity(request);
//                    if (newEntity == null) {
//                        log.info("StatisticMapper.toEntity returned null for request: {}", request);
//                        throw new IllegalStateException("Failed to map request to entity");
//                    }
//                    newEntity.setCreationTime(LocalDateTime.now());
//                    return newEntity;
//                });
//
//        if (entity.getHits() == null) {
//            entity.setHits(1);
//        } else {
//            entity.setHits(entity.getHits() + 1);
//        }
//
//        repository.save(entity);
//        log.info("Statistic saved successfully for URI: {}", request.getUri());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<StatisticResponse> getStatistic(final LocalDateTime start,
                                                final LocalDateTime end,
                                                final List<String> uris,
                                                final boolean unique) {
        log.info("Fetching statistics for uris {} from {} to {}, unique: {}",uris , start, end, unique);
        List<StatisticResponse> statistics;

        if (unique) {
            statistics = (uris != null && !uris.isEmpty())
                    ? repository.findStatisticsWithUniqueIpAndUriIn(start, end, uris)
                    : repository.findStatisticsWithUniqueIp(start, end);
            log.info("Fetched {} unique statistics records", statistics.size());
        } else {
            statistics = (uris != null && !uris.isEmpty())
                    ? repository.findByUriInAndCreationTimeBetween(uris, start, end)
                    : repository.findAllByCreationTimeBetween(start, end);
            log.info("Fetched {} statistics records", statistics.size());
        }

        return statistics;
    }
}
