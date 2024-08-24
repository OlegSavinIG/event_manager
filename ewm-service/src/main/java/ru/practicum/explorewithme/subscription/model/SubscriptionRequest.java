package ru.practicum.explorewithme.subscription.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long subscriberId;
    private SubscriptionStatus status;
}
