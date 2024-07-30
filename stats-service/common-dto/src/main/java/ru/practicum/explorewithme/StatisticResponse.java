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
     * The name of the application.
     */
    private String app;

    /**
     * The URI of the request.
     */
    private String uri;


    /**
     * The hits of the uri.
     */
    private Long hits;

}
