package ru.practicum.explorewithme.subscription.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.user.model.UserResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {
    private Long id;
    private UserResponse subscriber;
    private UserResponse user;
    private SubscriptionStatus status;
}
