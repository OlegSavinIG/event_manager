package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.StatisticEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticEntity, Long> {

    @Query("SELECT s FROM StatisticEntity s WHERE s.creationTime BETWEEN :start AND :end")
    List<StatisticEntity> findAllStatistic(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT s FROM StatisticEntity s WHERE s.creationTime BETWEEN :start AND :end AND s.uri IN :uris")
    List<StatisticEntity> findAllStatisticByUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("SELECT s FROM StatisticEntity s WHERE s.creationTime BETWEEN :start AND :end GROUP BY s.ip, s.app, s.uri, s.creationTime")
    List<StatisticEntity> findAllStatisticWithUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT s FROM StatisticEntity s WHERE s.creationTime BETWEEN :start AND :end AND s.uri IN :uris GROUP BY s.ip, s.app, s.uri, s.creationTime")
    List<StatisticEntity> findAllStatisticByUrisWithUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);
}
