package ru.practicum.explorewithme.subscription.service;

import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.subscription.model.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponse createSubscription(Long subId, Long userId);

    SubscriptionResponse approveSubscription(String approve, Long subscriptionId);

    void deleteSubscription(Long subscriptionId);

    List<EventResponse> getFriendEvents(Long subId, Long userId);
}
