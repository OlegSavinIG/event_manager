package ru.practicum.explorewithme.subscription.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.subscription.model.SubscriptionResponse;
import ru.practicum.explorewithme.subscription.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService service;

    @PostMapping("/{subId}")
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @NotNull @PathVariable Long subId,
            @NotNull @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createSubscription(subId, userId));
    }

    @PatchMapping("/update/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> approveSubscription(
            @RequestParam String approve,
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(service.approveSubscription(approve, subscriptionId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSubscription(@RequestParam Long subscriptionId) {
        service.deleteSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{subId}")
    public ResponseEntity<List<EventResponse>> getFriendEvents(
            @PathVariable Long subId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(service.getFriendEvents(subId, userId));
    }

}
