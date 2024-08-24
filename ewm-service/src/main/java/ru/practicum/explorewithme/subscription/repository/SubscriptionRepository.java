package ru.practicum.explorewithme.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.subscription.model.SubscriptionEntity;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
}
