package ru.practicum.explorewithme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
    private Long hits;

    /**
     * The creation time of the statistic record.
     */
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
}
