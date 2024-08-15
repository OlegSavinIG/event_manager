package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.StatisticResponse;
import ru.practicum.explorewithme.model.StatisticEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for handling StatisticEntity operations.
 */
@Repository
public interface StatisticRepository
        extends JpaRepository<StatisticEntity, Long> {
    /**
     * Retrieves statistics with unique IPs and specified URIs.
     *
     * @param uris  the list of URIs
     * @param start the start datetime
     * @param end   the end datetime
     * @return list of StatisticResponse
     */
    @Query("SELECT new ru.practicum.explorewithme"
            + ".StatisticResponse(s.app, s.uri, COUNT(DISTINCT s.uri) AS hits) "
            + "FROM StatisticEntity s "
            + "WHERE s.creationTime BETWEEN :start AND :end "
            + "AND s.uri IN :uris "
            + "GROUP BY s.app, s.uri "
            + "ORDER BY hits DESC")
    List<StatisticResponse> findStatisticsWithUniqueIpAndUriIn(
            @Param("uris")  List<String> uris,
            @Param("start")  LocalDateTime start,
            @Param("end")  LocalDateTime end);


    /**
     * Retrieves statistics with unique IPs within a date range.
     *
     * @param start the start datetime
     * @param end   the end datetime
     * @return list of StatisticResponse
     */
    @Query("SELECT new ru.practicum.explorewithme"
            + ".StatisticResponse(s.app, s.uri, COUNT(DISTINCT s.uri) AS hits) "
            + "FROM StatisticEntity s "
            + "WHERE s.creationTime BETWEEN :start AND :end "
            + "GROUP BY s.app, s.uri "
            + "ORDER BY hits DESC")
    List<StatisticResponse> findStatisticsWithUniqueIp(
            @Param("start")  LocalDateTime start,
            @Param("end")  LocalDateTime end);


    /**
     * Retrieves statistics for specified URIs within a date range.
     *
     * @param uris  the list of URIs
     * @param start the start datetime
     * @param end   the end datetime
     * @return list of StatisticResponse
     */
    @Query("SELECT new ru.practicum.explorewithme"
            + ".StatisticResponse(s.app, s.uri, COUNT(s.uri) AS hits) "
            + "FROM StatisticEntity s "
            + "WHERE s.creationTime BETWEEN :start AND :end "
            + "AND s.uri IN :uris "
            + "GROUP BY s.app, s.uri "
            + "ORDER BY hits DESC")
    List<StatisticResponse> findStatisticByUriIn(
            @Param("uris")  List<String> uris,
            @Param("start")  LocalDateTime start,
            @Param("end")  LocalDateTime end);

    /**
     * Retrieves statistics for specified URIs within a date range.
     *
     * @param uris  the list of URIs
     * @return list of StatisticResponse
     */
    @Query("SELECT new ru.practicum.explorewithme"
            + ".StatisticResponse(s.app, s.uri, COUNT(s.uri) AS hits) "
            + "FROM StatisticEntity s "
            + "WHERE s.uri IN :uris "
            + "GROUP BY s.app, s.uri "
            + "ORDER BY hits DESC")
    List<StatisticResponse> findStatisticByUriIn(
            @Param("uris") List<String> uris);

    /**
     * Retrieves all statistics within a date range.
     *
     * @param start the start datetime
     * @param end   the end datetime
     * @return list of StatisticResponse
     */
    @Query("SELECT new ru.practicum.explorewithme"
            + ".StatisticResponse(s.app, s.uri, COUNT(s.uri) AS hits) "
            + "FROM StatisticEntity s "
            + "WHERE s.creationTime BETWEEN :start AND :end "
            + "GROUP BY s.app, s.uri "
            + "ORDER BY hits DESC")
    List<StatisticResponse> findAllByCreationTimeBetween(
            @Param("start")  LocalDateTime start,
            @Param("end")  LocalDateTime end);
}
