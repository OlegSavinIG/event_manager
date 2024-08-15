package ru.practicum.explorewithme.user.controller.privateuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.user.request.model.ApproveRequestCriteria;
import ru.practicum.explorewithme.user.request.model.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.user.request.model.UserEventRequestDto;
import ru.practicum.explorewithme.user.service.privateuser.PrivateUserRequestService;

import java.util.List;

/**
 * Controller for handling private user operations related to event requests.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateUserRequestController {
    /**
     * Service for handling private user request operations.
     */
    private final PrivateUserRequestService service;

    /**
     * Retrieves event requests for a specific user and event.
     *
     * @param userId  the ID of the user
     * @param eventId the ID of the event
     * @return ResponseEntity with a list of UserEventRequestDto objects
     */
    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<UserEventRequestDto>> getEventRequests(
            @PathVariable final Long userId,
            @PathVariable final Long eventId) {
        return ResponseEntity.ok(service.getEventRequests(userId, eventId));
    }

    /**
     * Approves event requests for a specific user and event.
     *
     * @param userId   the ID of the user
     * @param eventId  the ID of the event
     * @param criteria the ApproveRequestCriteria
     *                 object containing approval criteria
     * @return ResponseEntity with the EventRequestStatusUpdateResult object
     */
    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> approveRequests(
            @RequestBody  final ApproveRequestCriteria criteria,
            @PathVariable final Long userId,
            @PathVariable final Long eventId) {
            log.info("Received request with criteria: {}", criteria);
            return ResponseEntity.ok(service.approveRequests(userId, eventId, criteria));
    }

    /**
     * Retrieves all requests made by a specific user.
     *
     * @param userId the ID of the user
     * @return ResponseEntity with a list of UserEventRequestDto objects
     */
    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<UserEventRequestDto>> getUserRequests(
            @PathVariable final Long userId) {
        return ResponseEntity.ok(service.getUserRequests(userId));
    }

    /**
     * Creates a new request for a specific user and event.
     *
     * @param userId  the ID of the user
     * @param eventId the ID of the event
     * @return ResponseEntity with the created UserEventRequestDto object
     */
    @PostMapping("/{userId}/requests")
    public ResponseEntity<UserEventRequestDto> createRequest(
            @PathVariable final Long userId,
            @RequestParam final Long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createRequest(userId, eventId));
    }

    /**
     * Cancels a request made by a specific user.
     *
     * @param userId    the ID of the user
     * @param requestId the ID of the request to cancel
     * @return ResponseEntity with the canceled UserEventRequestDto object
     */
    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<UserEventRequestDto> cancelRequest(
            @PathVariable final Long userId,
            @PathVariable final Long requestId) {
        return ResponseEntity.ok(service.cancelRequest(userId, requestId));
    }
}
