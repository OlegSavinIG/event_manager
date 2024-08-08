package ru.practicum.explorewithme.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.user.model.UserResponseWithEvent;

import java.time.LocalDateTime;

/**
 * DTO for a short representation of event responses.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseShort {

    /**
     * The annotation of the event.
     */
    private String annotation;

    /**
     * The date and time when the event will take place.
     */
    private LocalDateTime eventDate;

    /**
     * The category of the event.
     */
    private CategoryEntity category;

    /**
     * The number of confirmed requests for the event.
     */
    private Integer confirmedRequests;

    /**
     * The initiator of the event.
     */
    private UserResponseWithEvent initiator;

    /**
     * Indicates if the event is paid.
     */
    private Boolean paid;

    /**
     * The number of views of the event.
     */
    private long views;

    /**
     * The title of the event.
     */
    private String title;

    /**
     * The unique identifier of the event.
     */
    private Long id;
}
