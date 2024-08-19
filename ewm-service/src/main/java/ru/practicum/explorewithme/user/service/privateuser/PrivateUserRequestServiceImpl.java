package ru.practicum.explorewithme.user.service.privateuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.AlreadyExistException;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.exists.ExistChecker;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.repository.RequestRepository;
import ru.practicum.explorewithme.user.request.model.ApproveRequestCriteria;
import ru.practicum.explorewithme.user.request.model.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.user.request.model.RequestStatus;
import ru.practicum.explorewithme.user.request.model.UserEvenRequestMapper;
import ru.practicum.explorewithme.user.request.model.UserEventRequestDto;
import ru.practicum.explorewithme.user.request.model.UserEventRequestEntity;
import ru.practicum.explorewithme.user.service.admin.AdminUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link PrivateUserRequestService}
 * interface.
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
    public List<UserEventRequestDto> getEventRequests(
            final Long userId, final Long eventId) {
        log.info("Fetching event requests for event ID: {} by user ID: {}",
                eventId, userId);
        checker.isEventExists(eventId);
        checker.isUserExist(userId);

        EventResponse event = eventService.getEvent(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            return repository.findAllByEventId(eventId)
                    .orElseThrow(() -> new NotExistException(
                            "Event does not have requests"))
                    .stream()
                    .map(UserEvenRequestMapper::toDto)
                    .collect(Collectors.toList());
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
        log.info("Approving requests for event ID: {} by user ID: {} " +
                "with criteria: {}", eventId, userId, criteria);
        EventEntity event = approveRequestValidation(userId, eventId);

        List<UserEventRequestEntity> requests = repository.findAllById(
                criteria.getRequestIds());

        List<UserEventRequestDto> confirmedRequests = new ArrayList<>();
        List<UserEventRequestDto> rejectedRequests = new ArrayList<>();

        for (UserEventRequestEntity request : requests) {
            updateRequestStatus(request, criteria.getStatus());
            UserEventRequestDto dto = UserEvenRequestMapper.toDto(request);
            if (RequestStatus.CONFIRMED.equals(
                    dto.getStatus())) {
                confirmedRequests.add(dto);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            } else if (RequestStatus.REJECTED.equals(
                    dto.getStatus())) {
                rejectedRequests.add(dto);
                event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            }
        }
        eventRepository.save(event);
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserEventRequestDto> getUserRequests(final Long userId) {
        log.info("Fetching user requests for user ID: {}", userId);
        checker.isUserExist(userId);
        return repository.findAllByRequesterId(userId).stream()
                .map(UserEvenRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEventRequestDto createRequest(final Long userId,
                                             final Long eventId) {
        log.info("Creating request for event ID: {} by user ID: {}",
                eventId, userId);
        checker.isRequestExistsForInitiator(userId, eventId);
        checker.isThisInitiatorOfEvent(userId, eventId);

        EventEntity event = eventService.getEventEntity(eventId);
        validateEventForRequestCreation(event);

        UserEntity userEntity = adminUserService.findUserEntity(userId);
        UserEventRequestEntity eventRequestEntity =
                UserEventRequestEntity.builder()
                        .created(LocalDateTime.now())
                        .requester(userEntity)
                        .event(event)
                        .build();
        if (!event.getRequestModeration() ||
                event.getParticipantLimit() == 0) {
            eventRequestEntity.setStatus(RequestStatus.CONFIRMED);
        } else {
            eventRequestEntity.setStatus(RequestStatus.PENDING);
        }


//        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
//        eventRepository.save(event);

        UserEventRequestEntity saved = repository.save(eventRequestEntity);
        log.info("Request created with ID: {} status : {} request moderation: {}",
                saved.getId(), saved.getStatus(), event.getRequestModeration());
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

        UserEventRequestEntity entity = repository.findByIdAndRequesterId(
                        requestId, userId)
                .orElseThrow(() -> new NotExistException(
                        "Request not found"));

        if (entity.getStatus() == RequestStatus.CONFIRMED) {
            throw new ConflictException("Request already confirmed " +
                    "and cannot be cancelled");
        }

        entity.setStatus(RequestStatus.CANCELED);
        repository.save(entity);

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
        RequestStatus requestStatus = RequestStatus.valueOf(
                status.toUpperCase());
        entity.setStatus(requestStatus);
        repository.save(entity);
    }

    /**
     * Validates event and user before approving requests.
     *
     * @param userId  the user ID
     * @param eventId the event ID
     * @return Validated event entity.
     */
    private EventEntity approveRequestValidation(
            final Long userId, final Long eventId) {
        checker.isUserExist(userId);
        checker.isEventExists(eventId);

        EventEntity event = eventService.getEventEntity(eventId);
        log.info("Approving requests with participant limit: {} confirmed " +
                        "requests: {} request moderation: {}",
                event.getParticipantLimit(), event.getConfirmedRequests(),
                event.getRequestModeration());

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit()
                <= event.getConfirmedRequests()) {
            throw new ConflictException("Participants limit reached");
        }

        if (!event.getRequestModeration()) {
            throw new IllegalArgumentException("Event doesn't have moderation");
        }

        if (!event.getInitiator().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not the initiator of" +
                    " this event");
        }

        return event;
    }

    /**
     * Validates event for request creation.
     *
     * @param event event to validate.
     */
    private void validateEventForRequestCreation(final EventEntity event) {
        log.info("Validating event for request creation with participant " +
                        "limit: {} confirmed requests: {}",
                event.getParticipantLimit(), event.getConfirmedRequests());

        if (event.getState() == EventStatus.PENDING) {
            throw new AlreadyExistException("This event is not published");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit()
                <= event.getConfirmedRequests()) {
            throw new AlreadyExistException("Participants limit reached");
        }
    }
}
