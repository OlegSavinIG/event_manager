package ru.practicum.explorewithme.user.controller.privateuser;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.request.model.ApproveRequestCriteria;
import ru.practicum.explorewithme.user.request.model.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.user.request.model.UserEventRequestDto;
import ru.practicum.explorewithme.user.service.privateuser.PrivateUserRequestService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateUserRequestController {
    private final PrivateUserRequestService service;

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<UserEventRequestDto>> getEventRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return ResponseEntity.ok(service.getEventRequests(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> approveRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody ApproveRequestCriteria criteria
    ) {
        return ResponseEntity.ok(service.approveRequests(userId, eventId, criteria));
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<UserEventRequestDto>> getUserRequests(
            @PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserRequests(userId));
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<UserEventRequestDto> createRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        return ResponseEntity.ok(service.createRequest(userId, eventId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<UserEventRequestDto> cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(service.cancelRequest(userId, requestId));
    }
}
