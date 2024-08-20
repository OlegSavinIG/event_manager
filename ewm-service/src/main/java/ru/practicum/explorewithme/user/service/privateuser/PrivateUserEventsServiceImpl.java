package ru.practicum.explorewithme.user.service.privateuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.CategoryResponse;
import ru.practicum.explorewithme.category.model.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.model.mapper.EventMapper;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.AlreadyExistException;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.exists.ExistChecker;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.service.admin.AdminUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link PrivateUserEventsService} interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateUserEventsServiceImpl implements PrivateUserEventsService {

    /**
     * Repository for event operations.
     */
    private final EventRepository repository;
    /**
     * Service for admin user operations.
     */
    private final AdminUserService adminUserService;
    /**
     * Service for category operations.
     */
    private final CategoryService categoryService;
    /**
     * Service for existence checking.
     */
    private final ExistChecker checker;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventResponse> getEventsByUserId(final Long userId,
                                                 final Integer from,
                                                 final Integer size) {
        log.info("Fetching events for user ID: {}, from: {}, size: {}", userId,
                from, size);
        checker.isUserExist(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        Page<EventEntity> eventEntities = repository
                .findAllByInitiatorId(userId, pageable)
                .orElseThrow(() -> new NotExistException(
                        "This user does not have events"));
        List<EventResponse> responses = eventEntities.stream()
                .map(EventMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} events for user ID: {}", responses.size(), userId);
        return responses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventResponse getByUserIdAndEventId(final Long userId,
                                               final Long eventId) {
        log.info("Fetching event ID: {} for user ID: {}", eventId, userId);
        checker.isUserExist(userId);
        checker.isEventExists(eventId);
        EventEntity entity = repository
                .findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotExistException(
                        "This event does not exist"));
        log.info("Found event ID: {} for user ID: {}", eventId, userId);
        return EventMapper.toResponse(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional()
    public EventResponse createEvent(final EventRequest request,
                                     final Long userId) {
        log.info("Creating event for user ID: {}", userId);
        checker.isUserExist(userId);
        UserEntity userEntity = adminUserService.findUserEntity(userId);
        CategoryEntity category = categoryService.getCategoryEntity(
                request.getCategory());

        EventEntity entity = EventMapper.toEntity(request,
                category, userEntity);
        entity.setCreatedOn(LocalDateTime.now());
        entity.setState(EventStatus.PENDING);
        EventEntity eventEntity = repository.save(entity);

        log.info("Event created with ID: {} for user ID: {}",
                eventEntity.getId(),
                userId);
        EventResponse response = EventMapper.toResponse(eventEntity);
        response.setLocation(request.getLocation());
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public EventResponse updateEvent(final Long userId, final Long eventId,
                                     final EventRequest request) {
        log.info("Updating event ID: {} for user ID: {} with request: {}",
                eventId, userId, request);
        checker.isUserExist(userId);
        checker.isEventExists(eventId);
        EventEntity entity = repository
                .findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotExistException(
                        "This event does not exist"));
        if (entity.getState().equals(EventStatus.PUBLISHED)) {
             throw new AlreadyExistException("Event already published");
        }
        if (request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }
        if (request.getAnnotation() != null) {
            entity.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            entity.setEventDate(request.getEventDate());
        }
        if (request.getPaid() != null) {
            entity.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            entity.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            entity.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            entity.setState((request.getStateAction()));
        }
        if (request.getCategory() != null) {
            CategoryEntity category = categoryService.getCategoryEntity(
                    request.getCategory());
            entity.setCategory(category);
        }
        if (request.getStateAction() != null) {
            if (request.getStateAction().equals(EventStatus.SEND_TO_REVIEW)) {
                entity.setState(EventStatus.PENDING);
            }
            if (request.getStateAction().equals(EventStatus.CANCEL_REVIEW)) {
                entity.setState(EventStatus.CANCELED);
            }
        }
        EventEntity saved = repository.save(entity);
        log.info("Event ID: {} for user ID: {} updated", eventId, userId);
        return EventMapper.toResponse(saved);
    }

}
