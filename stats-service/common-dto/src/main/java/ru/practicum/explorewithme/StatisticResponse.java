package ru.practicum.explorewithme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for statistic data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticResponse {
    /**
     * The unique identifier of the statistic record.
     */
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
     * The creation time of the statistic record.
     */
    private LocalDateTime creationTime;
}
