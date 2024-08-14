package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.annotation.DefaultValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
     * Константа для максимальной длины  категории.
     */
    private static final int MAX_ANNOTATION_LENGTH = 2000;
    /**
     * Константа для максимальной длины  категории.
     */
    private static final int MAX_DESCRIPTION_LENGTH = 7000;
    /**
     * Константа для максимальной длины  категории.
     */
    private static final int MAX_TITLE_LENGTH = 120;
    /**
     * Константа для минимальной длины  категории.
     */
    private static final int MIN_LENGTH = 20;
    /**
     * Константа для минимальной длины  категории.
     */
    private static final int MIN_TITLE_LENGTH = 3;

    /**
     * The annotation of the event.
     */
    @NotNull(groups = DefaultValidation.class)
    @NotBlank(groups = DefaultValidation.class)
    @Size(min = MIN_LENGTH, max = MAX_ANNOTATION_LENGTH,
            groups = DefaultValidation.class)
    @Size(min = MIN_LENGTH, max = MAX_ANNOTATION_LENGTH)
    private String annotation;

    /**
     * The description of the event.
     */
    @NotNull(groups = DefaultValidation.class)
    @NotBlank(groups = DefaultValidation.class)
    @Size(min = MIN_LENGTH, max = MAX_DESCRIPTION_LENGTH,
            groups = DefaultValidation.class)
    @Size(min = MIN_LENGTH, max = MAX_DESCRIPTION_LENGTH)
    private String description;

    /**
     * The title of the event.
     */
    @NotNull(groups = DefaultValidation.class)
    @NotBlank(groups = DefaultValidation.class)
    @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH,
            groups = DefaultValidation.class)
    @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH)
    private String title;

    /**
     * The date and time when the event will take place.
     */
    @FutureOrPresent(groups = DefaultValidation.class)
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
    @PositiveOrZero(groups = DefaultValidation.class)
    @PositiveOrZero
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
    private EventStatus stateAction;
}
