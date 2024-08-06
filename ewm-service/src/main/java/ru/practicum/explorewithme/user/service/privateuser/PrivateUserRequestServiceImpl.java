package ru.practicum.explorewithme.user.service.privateuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.AlreadyExistException;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.exists.ExistChecker;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.repository.RequestRepository;
import ru.practicum.explorewithme.user.request.model.ApproveRequestCriteria;
import ru.practicum.explorewithme.user.request.model.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.user.request.model.UserEvenRequestMapper;
import ru.practicum.explorewithme.user.request.model.UserEventRequestDto;
import ru.practicum.explorewithme.user.request.model.UserEventRequestEntity;
import ru.practicum.explorewithme.user.service.admin.AdminUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link PrivateUserRequestService} interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateUserRequestServiceImpl
        implements PrivateUserRequestService {

    /**
     * Repository for user requests.
     */
    private final RequestRepository repository;
    /**
     * Service for event-related operations.
     */
    private final EventService eventService;
    /**
     * Repository for event entities.
     */
    private final EventRepository eventRepository;
    /**
     * Service for admin user operations.
     */
    private final AdminUserService adminUserService;
    /**
     * Checker for existence of various entities.
     */
    private final ExistChecker checker;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEventRequestDto> getEventRequests(final Long userId,
                                                      final Long eventId) {
        log.info("Fetching event requests for event ID: {} by user ID: {}",
                eventId, userId);
        checker.isEventExists(eventId);
        checker.isUserExist(userId);
        EventResponse event = eventService.getEvent(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            List<UserEventRequestEntity> eventRequestEntities =
                    repository.findAllByEventId(eventId)
                            .orElseThrow(() -> new NotExistException(
                                    "Event does not have requests"));
            List<UserEventRequestDto> requests = eventRequestEntities.stream()
                    .map(UserEvenRequestMapper::toDto)
                    .collect(Collectors.toList());
            log.info("Found {} requests for event ID: {}", requests.size(),
                    eventId);
            return requests;
        }
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EventRequestStatusUpdateResult approveRequests(
            final Long userId, final Long eventId,
            final ApproveRequestCriteria criteria) {
        log.info("Approving requests for event ID:"
                + " {} by user ID: {}", eventId, userId);
        checker.isUserExist(userId);
        EventEntity event = eventService.getEventEntity(eventId);
        if (event.getRequestModeration().equals(Boolean.FALSE)) {
            throw new IllegalArgumentException("Event doesn't have moderation");
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new IllegalArgumentException("Wrong userId or eventId");
        }
        List<Long> ids = criteria.getIds();
        String status = criteria.getStatus();
        List<UserEventRequestEntity> requests = repository.findAllById(ids);

        List<UserEventRequestDto> confirmedRequests = new ArrayList<>();
        List<UserEventRequestDto> rejectedRequests = new ArrayList<>();

        for (UserEventRequestEntity request : requests) {
            updateRequestStatus(request, status);
            UserEventRequestDto dto = UserEvenRequestMapper.toDto(request);
            if ("CONFIRMED".equalsIgnoreCase(dto.getStatus())) {
                confirmedRequests.add(dto);
            } else if ("REJECTED".equalsIgnoreCase(dto.getStatus())) {
                rejectedRequests.add(dto);
            }
        }
        if ("CONFIRMED".equalsIgnoreCase(status)) {
            int confirmedRequestsCount = event.getConfirmedRequests();
            event.setConfirmedRequests(
                    confirmedRequestsCount + confirmedRequests.size());
            eventRepository.save(event);
        }
        EventRequestStatusUpdateResult result =
                new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEventRequestDto> getUserRequests(final Long userId) {
        log.info("Fetching user requests for user ID: {}", userId);
        boolean existsById = repository.existsById(userId);
        if (!existsById) {
            throw new NotExistException("User not exist");
        }
        List<UserEventRequestEntity> eventRequestEntities =
                repository.findAllByRequesterId(userId);
        List<UserEventRequestDto> requests = eventRequestEntities.stream()
                .map(UserEvenRequestMapper::toDto)
                .collect(Collectors.toList());
        log.info("Found {} requests for user ID: {}", requests.size(), userId);
        return requests;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEventRequestDto createRequest(final Long userId,
                                             final Long eventId) {
        log.info("Creating request for event ID: {} by user ID: {}", eventId,
                userId);
        boolean existed = repository.existsByRequesterIdAndEventId(userId,
                eventId);
        if (existed) {
            throw new AlreadyExistException(
                    "User already has a request for this event");
        }
        EventEntity entity = eventService.getEventEntity(eventId);
        UserEntity userEntity = adminUserService.findUserEntity(userId);
        UserEventRequestEntity eventRequestEntity =
                UserEventRequestEntity.builder()
                        .status("PENDING")
                        .created(LocalDateTime.now())
                        .requester(userEntity)
                        .event(entity)
                        .build();
        UserEventRequestEntity saved = repository.save(eventRequestEntity);
        log.info("Request created with ID: {} for event ID: {} by user ID: {}",
                saved.getId(), eventId, userId);
        return UserEvenRequestMapper.toDto(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEventRequestDto cancelRequest(final Long userId,
                                             final Long requestId) {
        log.info("Cancelling request ID: {} by user ID: {}", requestId, userId);
        checker.isUserExist(userId);
        checker.isRequestExists(requestId);
        UserEventRequestEntity entity =
                repository.findByIdAndRequesterId(requestId, userId)
                        .orElseThrow(() ->
                                new NotExistException("Request not found"));
        entity.setStatus("CANCELED");
        repository.delete(entity);
        log.info("Request ID: {} cancelled by user ID: {}", requestId, userId);
        return UserEvenRequestMapper.toDto(entity);
    }

    /**
     * Updates the status of a user event request.
     *
     * @param entity the user event request entity
     * @param status the new status
     */
    private void updateRequestStatus(final UserEventRequestEntity entity,
                                     final String status) {
        entity.setStatus(status);
        repository.save(entity);
    }
}
