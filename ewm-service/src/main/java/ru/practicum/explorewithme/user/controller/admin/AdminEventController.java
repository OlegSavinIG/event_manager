package ru.practicum.explorewithme.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.user.model.EventSearchCriteriaForAdmin;
import ru.practicum.explorewithme.user.service.admin.AdminEventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Controller for handling administrative operations on events.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService service;

    /**
     * Retrieves events based on criteria, paginated.
     *
     * @param criteria search criteria for filtering events
     * @param from     index of the first result to retrieve (default 0)
     * @param size     maximum number of events to retrieve (default 10)
     * @return ResponseEntity with a list of EventResponse objects
     */
    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getEvents(
            @ModelAttribute EventSearchCriteriaForAdmin criteria,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(service.getEvents(criteria, from, size));
    }

    /**
     * Approves an event by its ID.
     *
     * @param request the EventRequest object containing updated event data
     * @param eventId the ID of the event to approve
     * @return ResponseEntity with the approved EventResponse object
     */
    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventResponse> approveEvent(
            @RequestBody EventRequest request,
            @PathVariable Long eventId) {
        EventResponse response = service.approveEvent(request, eventId);
        return ResponseEntity.ok(response);
    }
}
