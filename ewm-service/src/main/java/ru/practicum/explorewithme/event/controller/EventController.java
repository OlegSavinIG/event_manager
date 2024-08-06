package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.event.client.EventClient;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventResponseShort;
import ru.practicum.explorewithme.event.model.EventSearchCriteria;
import ru.practicum.explorewithme.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * REST controller for managing events.
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    /**
     * REST client for managing compilations.
     */
    private final EventService service;
    /**
     * REST service for managing compilations.
     */
    private final EventClient client;

    /**
     * Retrieves a list of events based on search criteria.
     *
     * @param criteria       the search criteria for events
     * @param from           the starting index of the result
     * @param size           the number of results to retrieve
     * @param servletRequest the HTTP servlet request
     * @return a response entity containing the list of event responses
     */
    @GetMapping
    public ResponseEntity<List<EventResponseShort>> getEvents(
            @ModelAttribute final EventSearchCriteria criteria,
            @PositiveOrZero @RequestParam(defaultValue = "0")
            final Integer from,
            @Positive @RequestParam(defaultValue = "10")
            final Integer size,
            final HttpServletRequest servletRequest
    ) {
        final String remoteAddr = servletRequest.getRemoteAddr();
        final String requestURI = servletRequest.getRequestURI();
        client.sendRequestData(remoteAddr, requestURI);
        return ResponseEntity.ok(service.getEvents(criteria, from, size));
    }

    /**
     * Retrieves a specific event by its ID.
     *
     * @param id             the ID of the event
     * @param servletRequest the HTTP servlet request
     * @return a response entity containing the event response
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(
            @PathVariable final Long id,
            final HttpServletRequest servletRequest
    ) {
        final String remoteAddr = servletRequest.getRemoteAddr();
        final String requestURI = servletRequest.getRequestURI();
        client.sendRequestData(remoteAddr, requestURI);
        return ResponseEntity.ok(service.getEvent(id));
    }
}
