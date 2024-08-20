package ru.practicum.explorewithme.user.service.privateuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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

    private final RequestRepository repository;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final AdminUserService adminUserService;
    private final ExistChecker checker;

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
        return List.of();
    }

    @Override
    @Transactional()
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
                updateRequestStatus(request, criteria.getStatus(), event);
                UserEventRequestDto dto = UserEvenRequestMapper.toDto(request);
                if (RequestStatus.CONFIRMED.equals(
                        dto.getStatus())) {
                    confirmedRequests.add(dto);
                } else if (RequestStatus.REJECTED.equals(
                        dto.getStatus())) {
                    rejectedRequests.add(dto);
                }
            }
            log.info("Confirmed requests: {}", event.getConfirmedRequests());
//        eventRepository.save(event);

            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(confirmedRequests)
                    .rejectedRequests(rejectedRequests)
                    .build();
        }

    @Override
    public List<UserEventRequestDto> getUserRequests(final Long userId) {
        log.info("Fetching user requests for user ID: {}", userId);
        checker.isUserExist(userId);
        return repository.findAllByRequesterId(userId).stream()
                .map(UserEvenRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public UserEventRequestDto createRequest(final Long userId,
                                             final Long eventId) {
        log.info("Creating request for event ID: {} by user ID: {}",
                eventId, userId);
        checker.isRequestAlreadyExist(userId, eventId);

        EventEntity event = eventService.getEventEntity(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new AlreadyExistException("This is your event");
        }
        validateEventForRequestCreation(event);

        UserEntity userEntity = adminUserService.findUserEntity(userId);
        UserEventRequestEntity eventRequestEntity =
                UserEventRequestEntity.builder()
                        .created(LocalDateTime.now())
                        .requester(userEntity)
                        .event(event)
                        .build();
        if (Boolean.FALSE.equals(event.getRequestModeration()) ||
                event.getParticipantLimit() == 0) {
            eventRequestEntity.setStatus(RequestStatus.CONFIRMED);
        } else {
            eventRequestEntity.setStatus(RequestStatus.PENDING);
        }
        UserEventRequestEntity saved = repository.save(eventRequestEntity);
        if (RequestStatus.CONFIRMED.equals(saved.getStatus())) {
            event.getConfirmedRequests().add(saved);
        }
        eventRepository.save(event);

        log.info("Request created with ID: {} for event ID: {} by user ID: {}",
                saved.getId(), eventId, userId);
        return UserEvenRequestMapper.toDto(saved);
    }

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

    private void updateRequestStatus(final UserEventRequestEntity entity,
                                     final String status,
                                     final EventEntity event) {
        RequestStatus requestStatus = RequestStatus.valueOf(
                status.toUpperCase());
        entity.setStatus(requestStatus);

        if (requestStatus == RequestStatus.CONFIRMED) {
            event.getConfirmedRequests().add(entity);
        } else {
            event.getConfirmedRequests().remove(entity);
        }
        repository.save(entity);
        eventRepository.save(event);
    }

    private EventEntity approveRequestValidation(
            final Long userId, final Long eventId) {
        checker.isUserExist(userId);
        checker.isEventExists(eventId);

        EventEntity event = eventService.getEventEntity(eventId);
        log.info("Approving requests");

        long confirmedCount = event.getConfirmedRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.CONFIRMED)
                .count();

        if (event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() <= confirmedCount) {
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

    private void validateEventForRequestCreation(final EventEntity event) {
        log.info("Validating event for request creation with moderation: {}",
                event.getRequestModeration());

        long confirmedCount = event.getConfirmedRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.CONFIRMED)
                .count();

        if (event.getState() == EventStatus.PENDING) {
            throw new AlreadyExistException("This event is not published");
        }

        if (event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() <= confirmedCount) {
            throw new AlreadyExistException("Participants limit reached");
        }
    }
}
