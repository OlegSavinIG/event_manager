package ru.practicum.explorewithme.user.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) representing a user event request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEventRequestDto {
    private Long id;

    /**
     * Date and time when the request was created.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    /**
     * ID of the event associated with the request.
     */
    private Long event;

    /**
     * ID of the user who made the request.
     */
    private Long requester;

    /**
     * Status of the request.
     */
    private String status;
}
