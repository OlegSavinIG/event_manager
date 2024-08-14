package ru.practicum.explorewithme.user.controller.privateuser;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.annotation.DefaultValidation;
import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.user.service.privateuser.PrivateUserEventsService;

import java.util.List;

/**
 * Controller for handling private user operations related to events.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateUserEventsController {

    /**
     * REST service for managing compilations by admin.
     */
    private final PrivateUserEventsService service;

    /**
     * Retrieves events for a specific user, paginated.
     *
     * @param userId the ID of the user
     * @param from   index of the first result (default 0)
     * @param size   maximum number of events (default 10)
     * @return ResponseEntity with a list of EventResponse objects
     */
    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventResponse>> getEventsByUserId(
            @PathVariable final Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0")
            final Integer from,
            @Positive @RequestParam(defaultValue = "10")
            final Integer size) {
        return ResponseEntity.ok(service
                .getEventsByUserId(userId, from, size));
    }

    /**
     * Retrieves a specific event by user ID and event ID.
     *
     * @param userId  the ID of the user
     * @param eventId the ID of the event
     * @return ResponseEntity with the EventResponse object
     */
    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventResponse> getByUserIdAndEventId(
            @PathVariable final Long userId,
            @PathVariable final Long eventId) {
        return ResponseEntity.ok(service
                .getByUserIdAndEventId(userId, eventId));
    }

    /**
     * Creates a new event for a specific user.
     *
     * @param userId  the ID of the user
     * @param request the EventRequest object containing event data
     * @return ResponseEntity with the created EventResponse object
     */
    @PostMapping("/{userId}/events")
    public ResponseEntity<EventResponse> createEvent(
            @PathVariable final Long userId,
            @Validated(DefaultValidation.class)
            @RequestBody final EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service
                .createEvent(request, userId));
    }

    /**
     * Updates an existing event for a specific user.
     *
     * @param userId  the ID of the user
     * @param eventId the ID of the event to update
     * @param request the EventRequest object containing updated event data
     * @return ResponseEntity with the updated EventResponse object
     */
    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable final Long userId,
            @PathVariable final Long eventId,
            @Valid @RequestBody final EventRequest request) {
        return ResponseEntity.ok(service
                .updateEvent(userId, eventId, request));
    }
}
