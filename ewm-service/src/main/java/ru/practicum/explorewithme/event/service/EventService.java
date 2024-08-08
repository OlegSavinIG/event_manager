package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventResponseShort;
import ru.practicum.explorewithme.event.model.EventSearchCriteria;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Service interface for managing events.
 */
public interface EventService {

    /**
     * Retrieves a list of events based on search criteria.
     *
     * @param criteria the search criteria for events
     * @param from     the starting index of the result
     * @param size     the number of results to retrieve
     * @return the list of short event responses
     */
    List<EventResponseShort> getEvents(EventSearchCriteria criteria,
                                       Integer from, Integer size);

    /**
     * Retrieves a specific event by its ID.
     *
     * @param id the ID of the event
     * @return the event response
     */
    EventResponse getEvent(Long id);

    /**
     * Retrieves a specific event entity by its ID.
     *
     * @param id the ID of the event
     * @return the event entity
     */
    EventEntity getEventEntity(Long id);

    /**
     * Retrieves events by their IDs.
     *
     * @param ids the list of event IDs
     * @return the list of event responses
     */
    List<EventResponse> getEventsByIds(List<Long> ids);

    /**
     * Retrieves event entities by their IDs.
     *
     * @param ids the list of event IDs
     * @return the list of event entities
     */
    List<EventEntity> getEventEntities(List<Long> ids);

    void saveStatistic(HttpServletRequest servletRequest);
}
