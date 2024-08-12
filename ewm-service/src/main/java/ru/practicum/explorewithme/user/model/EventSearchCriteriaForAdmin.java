package ru.practicum.explorewithme.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.explorewithme.event.model.EventStatus;

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
    private List<Integer> users;

    /**
     * List of states (statuses) to filter events by.
     */
    private List<EventStatus> states;

    /**
     * List of category IDs to filter events by.
     */
    private List<Integer> categories;

    /**
     * Start of the date and time range to filter events.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;

    /**
     * End of the date and time range to filter events.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;
}
