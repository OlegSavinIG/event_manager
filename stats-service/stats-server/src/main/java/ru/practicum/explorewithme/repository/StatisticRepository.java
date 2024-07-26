package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.StatisticEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for managing StatisticEntity entities.
 */
@Repository
public interface StatisticRepository
        extends JpaRepository<StatisticEntity, Long> {

    /**
     * Retrieves all statistics
     * between the specified start and end times.
     *
     * @param start the start time for filtering statistics
     * @param end   the end time for filtering statistics
     * @return the list of statistic entities
     */
    @Query("SELECT s FROM StatisticEntity s "
            + "WHERE s.creationTime BETWEEN :start AND :end")
    List<StatisticEntity> findAllStatistic(
            @Param("start")  LocalDateTime start,
            @Param("end")  LocalDateTime end);

    /**
     * Retrieves statistics
     * by URIs between the specified start and end times.
     *
     * @param start the start time for filtering statistics
     * @param end   the end time for filtering statistics
     * @param uris  the list of URIs to filter by
     * @return the list of statistic entities
     */
    @Query("SELECT s FROM StatisticEntity s "
            + "WHERE s.creationTime BETWEEN :start AND :end AND s.uri IN :uris")
    List<StatisticEntity> findAllStatisticByUris(
            @Param("start")  LocalDateTime start,
            @Param("end")  LocalDateTime end,
            @Param("uris")  List<String> uris);

    /**
     * Retrieves unique statistics by IP, app, URI, and creation time.
     *
     * @param start the start time for filtering statistics
     * @param end   the end time for filtering statistics
     * @return the list of unique statistic entities
     */
    @Query("SELECT s FROM StatisticEntity s "
            + "WHERE s.creationTime BETWEEN :start "
            + "AND :end GROUP BY s.ip, s.app, s.uri, s.creationTime")
    List<StatisticEntity> findAllStatisticWithUniqueIp(
            @Param("start")  LocalDateTime start,
            @Param("end")  LocalDateTime end);

    /**
     * Retrieves unique statistics
     * by URIs between the specified start and end
     * times.
     *
     * @param start the start time for filtering statistics
     * @param end   the end time for filtering statistics
     * @param uris  the list of URIs to filter by
     * @return the list of unique statistic entities
     */
    @Query("SELECT s FROM StatisticEntity s"
            + " WHERE s.creationTime BETWEEN :start AND :end "
            + "AND s.uri IN :uris GROUP BY s.ip, s.app, s.uri, s.creationTime")
    List<StatisticEntity> findAllStatisticByUrisWithUniqueIp(
            @Param("start")  LocalDateTime start,
            @Param("end")  LocalDateTime end,
            @Param("uris")  List<String> uris);
}
