package ru.practicum.explorewithme.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for event search criteria.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventSearchCriteria {

    /**
     * The text to search for in event titles and descriptions.
     */
    private String text;

    /**
     * The list of category IDs to filter the events.
     */
    private List<Integer> categories;

    /**
     * Indicates if the event should be paid.
     */
    private Boolean paid;

    /**
     * The start of the date range to filter events.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;

    /**
     * The end of the date range to filter events.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;

    /**
     * Indicates if only available events should be included.
     */
    @Builder.Default
    private Boolean onlyAvailable = false;

    /**
     * The sorting option for the search results.
     */
    private String sort;
}
