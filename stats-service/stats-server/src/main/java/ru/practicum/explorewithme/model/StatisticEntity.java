package ru.practicum.explorewithme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Entity representing a statistic record.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "statistic")
public class StatisticEntity {
    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the application.
     */
    private String app;

    /**
     * The URI of the request.
     */
    private String uri;

    /**
     * The IP address of the client.
     */
    private String ip;

    /**
     * The hits of the uri.
     */
    @Transient
    private long hits;

    /**
     * The creation time of the statistic record.
     */
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
}
