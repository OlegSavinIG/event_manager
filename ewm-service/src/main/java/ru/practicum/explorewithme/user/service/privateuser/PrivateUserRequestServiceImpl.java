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
import ru.practicum.explorewithme.user.request.model.*;
import ru.practicum.explorewithme.user.service.admin.AdminUserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateUserRequestServiceImpl implements PrivateUserRequestService {
    private final RequestRepository repository;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final AdminUserService adminUserService;
    private final ExistChecker checker;

    @Override
    public List<UserEventRequestDto> getEventRequests(Long userId, Long eventId) {
        log.info("Fetching event requests for event ID: {} by user ID: {}", eventId, userId);
        checker.isEventExists(eventId);
        checker.isUserExist(userId);
        EventResponse event = eventService.getEvent(eventId);
        if (event.getInitiator().getId() == userId) {
            List<UserEventRequestEntity> eventRequestEntities = repository.findAllByEventId(eventId)
                    .orElseThrow(() -> new NotExistException("Event does not have requests"));
            List<UserEventRequestDto> requests = eventRequestEntities.stream()
                    .map(UserEvenRequestMapper::toDto)
                    .collect(Collectors.toList());
            log.info("Found {} requests for event ID: {}", requests.size(), eventId);
            return requests;
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult approveRequests(
            Long userId, Long eventId, ApproveRequestCriteria criteria) {
        log.info("Approving requests for event ID: {} by user ID: {}", eventId, userId);
        checker.isUserExist(userId);
        EventEntity event = eventService.getEventEntity(eventId);
        if (event.getRequestModeration().equals(Boolean.FALSE)) {
            throw new IllegalArgumentException("Event doesn't have moderation");
        }
        if (event.getInitiator().getId() != userId) {
            throw new IllegalArgumentException("Wrong userId or eventId");
        }
        List<Long> ids = criteria.getIds();
        String status = criteria.getStatus();
        ExecutorService executor = Executors
                .newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<UserEventRequestEntity> requests = repository.findAllById(ids);
        try {
            List<Callable<Void>> tasks = requests.stream()
                    .map(request -> (Callable<Void>) () -> {
                                updateRequestStatus(request, status);
                                return null;
                            }
                    )
                    .collect(Collectors.toList());
            List<Future<Void>> futures = executor.invokeAll(tasks);
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            log.error("Error approving requests: {}", e.getMessage());
        } finally {
            executor.shutdown();
        }
        if (status.equals("CONFIRMED")) {
            int confirmedRequests = event.getConfirmedRequests();
            event.setConfirmedRequests(confirmedRequests + requests.size());
            eventRepository.save(event);
        }
        return null;
    }

    @Override
    public List<UserEventRequestDto> getUserRequests(Long userId) {
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

    @Override
    public UserEventRequestDto createRequest(Long userId, Long eventId) {
        log.info("Creating request for event ID: {} by user ID: {}", eventId, userId);
        boolean existed = repository.existsByRequesterIdAndEventId(userId, eventId);
        if (existed) {
            throw new AlreadyExistException("User already has a request for this event");
        }
        EventEntity entity = eventService.getEventEntity(eventId);
        UserEntity userEntity = adminUserService.findUserEntity(userId);
        UserEventRequestEntity eventRequestEntity = UserEventRequestEntity.builder()
                .status("PENDING")
                .created(LocalDateTime.now())
                .requester(userEntity)
                .event(entity)
                .build();
        UserEventRequestEntity saved = repository.save(eventRequestEntity);
        log.info("Request created with ID: {} for event ID: {} by user ID: {}", saved.getId(), eventId, userId);
        return UserEvenRequestMapper.toDto(saved);
    }

    @Override
    public UserEventRequestDto cancelRequest(Long userId, Long requestId) {
        log.info("Cancelling request ID: {} by user ID: {}", requestId, userId);
        checker.isUserExist(userId);
        checker.isRequestExists(requestId);
        UserEventRequestEntity entity = repository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotExistException("Request not found"));
        entity.setStatus("CANCELED");
        repository.delete(entity);
        log.info("Request ID: {} cancelled by user ID: {}", requestId, userId);
        return UserEvenRequestMapper.toDto(entity);
    }

    private void updateRequestStatus(UserEventRequestEntity entity, String status) {
        entity.setStatus(status);
        repository.save(entity);
    }
}
