package ru.practicum.explorewithme.user.controller.privateuser;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.annotation.DefaultValidation;
import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.user.service.privateuser.PrivateUserEventsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Controller for handling private user operations related to events.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateUserEventsController {
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
            @PathVariable Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getEventsByUserId(userId, from, size));
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
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return ResponseEntity.ok(service.getByUserIdAndEventId(userId, eventId));
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
            @PathVariable Long userId,
            @Validated(DefaultValidation.class) @RequestBody EventRequest request) {
        return ResponseEntity.ok(service.createEvent(request, userId));
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
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequest request) {
        return ResponseEntity.ok(service.updateEvent(userId, eventId, request));
    }
}
