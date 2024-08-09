package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.annotation.DefaultValidation;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO for event requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    /**
     * The annotation of the event.
     */
    @NotNull(groups = DefaultValidation.class)
    @NotBlank(groups = DefaultValidation.class)
    @Size(min = 20, max = 2000)
    private String annotation;

    /**
     * The description of the event.
     */
    @NotNull(groups = DefaultValidation.class)
    @NotBlank(groups = DefaultValidation.class)
    @Size(min = 20, max = 7000)
    private String description;

    /**
     * The title of the event.
     */
    @NotNull(groups = DefaultValidation.class)
    @NotBlank(groups = DefaultValidation.class)
    @Size(min = 3, max = 120)
    private String title;

    /**
     * The date and time when the event will take place.
     */
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * Indicates if the event is paid.
     */
    private Boolean paid;

    /**
     * The participant limit for the event.
     */
    private Integer participantLimit;

    /**
     * Indicates if the event requires request moderation.
     */
    private Boolean requestModeration;

    /**
     * The ID of the category for the event.
     */
    private Integer category;

    /**
     * The state action for the event.
     */
    private EventStatus state;
}
