package ru.practicum.explorewithme.user.service.privateuser;

import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;

import java.util.List;

/**
 * Service interface for private user event operations.
 */
public interface PrivateUserEventsService {

    /**
     * Retrieves events for a specific user, paginated.
     *
     * @param userId the ID of the user
     * @param from   the starting index of the result
     * @param size   the number of results to retrieve
     * @return the list of event responses
     */
    List<EventResponse> getEventsByUserId(Long userId, Integer from, Integer size);

    /**
     * Retrieves an event by user ID and event ID.
     *
     * @param userId  the ID of the user
     * @param eventId the ID of the event
     * @return the event response
     */
    EventResponse getByUserIdAndEventId(Long userId, Long eventId);

    /**
     * Creates a new event for a user.
     *
     * @param request the event request
     * @param userId  the ID of the user
     * @return the created event response
     */
    EventResponse createEvent(EventRequest request, Long userId);

    /**
     * Updates an existing event for a user.
     *
     * @param userId   the ID of the user
     * @param eventId  the ID of the event
     * @param request  the event request with updated data
     * @return the updated event response
     */
    EventResponse updateEvent(Long userId, Long eventId, EventRequest request);
}
