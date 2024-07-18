package ru.practicum.explorewithme.user.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Criteria object for searching events with administrative privileges.
 */
@Getter
@Setter
public class EventSearchCriteriaForAdmin {
    /**
     * List of user IDs to filter events by.
     */
    private List<Long> users;

    /**
     * List of states (statuses) to filter events by.
     */
    private List<String> states;

    /**
     * List of category IDs to filter events by.
     */
    private List<Integer> categories;

    /**
     * Start of the date and time range to filter events.
     */
    private LocalDateTime rangeStart;

    /**
     * End of the date and time range to filter events.
     */
    private LocalDateTime rangeEnd;
}
