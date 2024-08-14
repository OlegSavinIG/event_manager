package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.StatisticRequest;
import ru.practicum.explorewithme.StatisticResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service interface for handling statistics.
 */
public interface StatisticService {

    /**
     * Saves a statistic.
     *
     * @param request the statistic request
     */
    void saveStatistic(StatisticRequest request);

    /**
     * Retrieves statistics.
     *
     * @param start  the start date and time
     * @param end    the end date and time
     * @param uris   the list of URIs
     * @param unique whether to count only unique hits
     * @return the list of statistics
     */
    List<StatisticResponse> getStatistic(
            LocalDateTime start,
            LocalDateTime end, List<String> uris, boolean unique);

/**
 * Retrieves statistics.
 *
 * @param uris  uris
 * @return  Map<Long, Long> event views
 */
    Map<Long, Long> getEventViews(List<String> uris);
}
