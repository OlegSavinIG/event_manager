package ru.practicum.explorewithme.subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.mapper.EventMapper;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exists.ExistChecker;
import ru.practicum.explorewithme.subscription.model.SubscriptionEntity;
import ru.practicum.explorewithme.subscription.model.SubscriptionResponse;
import ru.practicum.explorewithme.subscription.model.SubscriptionStatus;
import ru.practicum.explorewithme.subscription.model.mapper.SubscriptionMapper;
import ru.practicum.explorewithme.subscription.repository.SubscriptionRepository;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.service.admin.AdminUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final ExistChecker checker;
    private final SubscriptionRepository repository;
    private final AdminUserService userService;
    private final EventService eventService;

    @Override
    public SubscriptionResponse createSubscription(Long subId, Long userId) {
        checker.isUserExist(subId);
        checker.isUserExist(userId);
        UserEntity userEntity = userService.findUserEntity(userId);
        UserEntity subscriber = userService.findUserEntity(subId);
        SubscriptionEntity entity = SubscriptionEntity.builder()
                .user(userEntity)
                .subscriber(subscriber)
                .status(SubscriptionStatus.PENDING)
                .build();
        SubscriptionEntity saved = repository.save(entity);
        return SubscriptionMapper.toResponse(saved);
    }

    @Override
    public SubscriptionResponse approveSubscription(String approve, Long subscriptionId) {
        checker.isSubscriptionExists(subscriptionId);
        SubscriptionEntity entity = repository.findById(subscriptionId).get();
        if (approve.equalsIgnoreCase(SubscriptionStatus.APPROVED.name())) {
            entity.setStatus(SubscriptionStatus.APPROVED);

            updateFriendList(entity.getUser().getId(), entity.getSubscriber().getId());

            SubscriptionEntity saved = repository.save(entity);
            return SubscriptionMapper.toResponse(saved);
        }
        if (approve.equalsIgnoreCase(SubscriptionStatus.REJECTED.name())) {
            entity.setStatus(SubscriptionStatus.CANCELLED);
            repository.save(entity);
            return SubscriptionMapper.toResponse(entity);
        }
        throw new IllegalArgumentException("Wrong parameters");
    }

    @Override
    public void deleteSubscription(Long subscriptionId) {
        checker.isSubscriptionExists(subscriptionId);
        SubscriptionEntity entity = repository.findById(subscriptionId).get();
        deleteFromFriendList(entity.getUser().getId(), entity.getSubscriber().getId());
        repository.deleteById(subscriptionId);
    }

    @Override
    public List<EventResponse> getFriendEvents(Long subId, Long userId) {
        checker.isUserExist(subId);
        checker.isUserExist(userId);
        UserEntity subscriber = userService.findUserEntity(subId);
        UserEntity user = userService.findUserEntity(userId);
        boolean contains = subscriber.getFriends().contains(user);
        if (!contains) {
            throw new IllegalArgumentException("You not subscribed to this user");
        }
        List<EventEntity> eventEntities = eventService.getEventEntities(List.of(userId));
        return eventEntities.stream()
                .map(EventMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void updateFriendList(Long userId, Long subscriberId) {
        UserEntity userEntity = userService.findUserEntity(userId);
        UserEntity subscriberEntity = userService.findUserEntity(subscriberId);
        subscriberEntity.getFriends().add(userEntity);
        userService.saveUser(subscriberEntity);
    }

    private void deleteFromFriendList(Long userId, Long subscriberId) {
        UserEntity userEntity = userService.findUserEntity(userId);
        UserEntity subscriberEntity = userService.findUserEntity(subscriberId);
        subscriberEntity.getFriends().remove(userEntity);
        userService.saveUser(subscriberEntity);
    }
}
