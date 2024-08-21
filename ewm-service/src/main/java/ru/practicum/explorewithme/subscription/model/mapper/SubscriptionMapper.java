package ru.practicum.explorewithme.subscription.model.mapper;

import ru.practicum.explorewithme.subscription.model.SubscriptionEntity;
import ru.practicum.explorewithme.subscription.model.SubscriptionResponse;
import ru.practicum.explorewithme.user.model.mapper.UserMapper;

public class SubscriptionMapper {
    public static SubscriptionResponse toResponse(
            SubscriptionEntity entity) {
       return SubscriptionResponse.builder()
                .id(entity.getId())
                .user(UserMapper.toResponse(entity.getUser()))
                .subscriber(UserMapper.toResponse(entity.getSubscriber()))
                .status(entity.getStatus())
                .build();
    }
}
