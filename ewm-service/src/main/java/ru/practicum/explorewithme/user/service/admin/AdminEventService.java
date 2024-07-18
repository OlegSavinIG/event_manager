package ru.practicum.explorewithme.user.service.admin;

import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.user.model.EventSearchCriteriaForAdmin;

import java.util.List;

/**
 * Service interface for admin event operations.
 */
public interface AdminEventService {

    /**
     * Retrieves events based on search criteria.
     *
     * @param criteria the search criteria for events
     * @param from     the starting index of the result
     * @param size     the number of results to retrieve
     * @return the list of event responses
     */
    List<EventResponse> getEvents(
            EventSearchCriteriaForAdmin criteria, Integer from, Integer size);

    /**
     * Approves an event.
     *
     * @param request the event request containing the event details
     * @param eventId the ID of the event to approve
     * @return the approved event response
     */
    EventResponse approveEvent(EventRequest request, Long eventId);
}
