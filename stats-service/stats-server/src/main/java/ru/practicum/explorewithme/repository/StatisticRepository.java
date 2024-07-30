package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.StatisticResponse;
import ru.practicum.explorewithme.model.StatisticEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticEntity, Long> {

    @Query("SELECT new ru.practicum.explorewithme.StatisticResponse(s.app, s.uri, COUNT(s)) " +
            "FROM StatisticEntity s WHERE s.creationTime BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri ORDER BY COUNT(s) DESC")
    List<StatisticResponse> findStatisticsWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.StatisticResponse(s.app, s.uri, COUNT(s)) " +
            "FROM StatisticEntity s WHERE s.creationTime BETWEEN :start AND :end " +
            "AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri ORDER BY COUNT(s) DESC")
    List<StatisticResponse> findStatisticsWithUniqueIpAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.explorewithme.StatisticResponse(s.app, s.uri, COUNT(s)) " +
            "FROM StatisticEntity s WHERE s.creationTime BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri ORDER BY COUNT(s) DESC")
    List<StatisticResponse> findAllByCreationTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.StatisticResponse(s.app, s.uri, COUNT(s)) " +
            "FROM StatisticEntity s WHERE s.creationTime BETWEEN :start AND :end " +
            "AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri ORDER BY COUNT(s) DESC")
    List<StatisticResponse> findByUriInAndCreationTimeBetween(List<String> uris, LocalDateTime start, LocalDateTime end);
}
