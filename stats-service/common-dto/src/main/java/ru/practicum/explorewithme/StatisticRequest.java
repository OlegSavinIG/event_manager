package ru.practicum.explorewithme;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Request DTO for statistic data.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticRequest {
    /**
     * The name of the application.
     */
    @NotNull
    @NotBlank
    private String app;

    /**
     * The URI of the request.
     */
    @NotNull
    @NotBlank
    private String uri;

    /**
     * The IP address of the client.
     */
    @NotNull
    @NotBlank
    private String ip;

    /**
     * The creation time of the statistic record.
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;
}
