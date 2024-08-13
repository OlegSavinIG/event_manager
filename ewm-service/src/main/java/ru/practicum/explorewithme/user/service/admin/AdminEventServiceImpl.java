package ru.practicum.explorewithme.user.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.model.mapper.EventMapper;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.specification.EventSpecification;
import ru.practicum.explorewithme.exception.AlreadyExistException;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.exists.ExistChecker;
import ru.practicum.explorewithme.user.model.EventSearchCriteriaForAdmin;
import ru.practicum.explorewithme.user.repository.AdminEventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link AdminEventService} interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {

    /**
     * Repository for admin event operations.
     */
    private final AdminEventRepository repository;

    /**
     * Repository for event operations.
     */
    private final EventRepository eventRepository;

    /**
     * Repository for category operations.
     */
    private final CategoryRepository categoryRepository;
    /**
     * Exist checker for category operations.
     */
    private final ExistChecker checker;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getEvents(
            final EventSearchCriteriaForAdmin criteria,
            final Integer from,
            final Integer size) {
        log.info("Fetching events with criteria: {}", criteria);
        Pageable pageable = PageRequest.of(from / size, size);
        Specification<EventEntity> spec = Specification.where(null);

        if (criteria.getUsers() != null && !criteria.getUsers().isEmpty()) {
            spec = spec.and(EventSpecification.hasUsers(
                    criteria.getUsers().stream().map(Integer::longValue)
                    .collect(Collectors.toList())));
        }

        if (criteria.getStates() != null && !criteria.getStates().isEmpty()) {
            spec = spec.and(EventSpecification.hasStates(criteria.getStates()));
        }

        if (criteria.getCategories() != null && !criteria
                .getCategories().isEmpty()) {
            spec = spec.and(EventSpecification
                    .hasCategories(criteria.getCategories()));
        }

        if (criteria.getRangeStart() != null) {
            spec = spec.and(EventSpecification
                    .dateAfter(criteria.getRangeStart()));
        }

        if (criteria.getRangeEnd() != null) {
            spec = spec.and(EventSpecification
                    .dateBefore(criteria.getRangeEnd()));
        }

        Page<EventEntity> eventEntities = repository.findAll(spec, pageable);
        List<EventResponse> response = eventEntities.stream()
                .map(EventMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Fetched {} events", response.size());
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EventResponse approveEvent(
            final EventRequest request, final Long eventId) {
        log.info("Approving event with id: {}",
                eventId);
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotExistException(
                        "Event with id=" + eventId + " was not found"));
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new AlreadyExistException("Event already approved");
        }
        if (event.getState().equals(EventStatus.REJECTED)) {
            throw new AlreadyExistException("Event rejected");
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            CategoryEntity category = categoryRepository.findById(
                            request.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Category not found"));
            event.setCategory(category);
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getStateAction() != null
                &&
                (request.getStateAction().name().equals("PUBLISH_EVENT")
                        ||
                        request.getStateAction().name().equals(
                                "REJECT_EVENT"))) {
            handleStateAction(request.getStateAction(), event);
        }

        eventRepository.save(event);
        log.info("Approved event with id: {}, and status {} ",
                eventId, event.getState());
        return EventMapper.toResponse(event);
    }

    /**
     * Handles state actions for events.
     *
     * @param stateAction the state action to perform
     * @param event       the event entity to update
     */
    private void handleStateAction(
            final EventStatus stateAction, final EventEntity event) {
        switch (stateAction) {
            case PUBLISH_EVENT:
                if (!event.getState().name().equals("PENDING")) {
                    log.info("Invalid state action: {}", stateAction);
                    throw new IllegalArgumentException(
                            "Invalid state action: " + stateAction);
                }
                event.setState(EventStatus.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case REJECT_EVENT:
                if (event.getState().name().equals("PUBLISHED")) {
                    log.info("Invalid state action: {}", stateAction);
                    throw new IllegalArgumentException(
                            "Invalid state action: " + stateAction);
                }
                event.setState(EventStatus.REJECTED);
                break;
            default:
                break;
        }
    }
}
