package ru.practicum.explorewithme.user.service.admin;

import jakarta.annotation.PostConstruct;
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
import ru.practicum.explorewithme.user.model.EventSearchCriteriaForAdmin;
import ru.practicum.explorewithme.user.repository.AdminEventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link AdminEventService} interface.
 * Provides methods for managing events by an admin.
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

    @Transactional
    private void warmUp() {
        log.info("Warming up by fetching all events and counting.");
        repository.findAll();
        repository.count();
    }

    @PostConstruct
    public void init() {
        log.info("AdminEventServiceImpl initialized and warming up.");
        warmUp();
    }

    /**
     * Retrieves a list of events based on the provided criteria.
     *
     * @param criteria       the search criteria for filtering events
     * @param from           the offset for pagination
     * @param size           the size of the page
     * @return a list of event responses matching the criteria
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getEvents(
            final EventSearchCriteriaForAdmin criteria,
            final Integer from,
            final Integer size) {
            log.info("Fetching events with criteria: {}", criteria.getCategories());
            Pageable pageable = PageRequest.of(from / size, size);
            Specification<EventEntity> spec = buildSpecification(criteria);

            Page<EventEntity> eventEntities = repository.findAll(spec, pageable);
        return eventEntities.stream()
                    .map(EventMapper::toResponse)
                    .collect(Collectors.toList());
    }

    /**
     * Approves an event by updating its details and state.
     *
     * @param request the request containing updated event details
     * @param eventId the ID of the event to approve
     * @return the updated event response
     */
    @Override
    @Transactional
    public EventResponse approveEvent(
            final EventRequest request, final Long eventId) {
        log.info("Approving event with id: {}", eventId);
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotExistException(
                        "Event with id=" + eventId + " was not found"));

        validateEventState(event);
        updateEventDetails(request, event);
        eventRepository.save(event);

        log.info("Approved event with id: {}, and status {}",
                eventId, event.getState());
        return EventMapper.toResponse(event);
    }


    /**
     * Validates the current state of the event before proceeding with approval.
     *
     * @param event the event entity to validate
     * @throws AlreadyExistException if the event is already published or rejected
     */
    private void validateEventState(EventEntity event) {
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new AlreadyExistException("Event already approved");
        }
        if (event.getState().equals(EventStatus.REJECTED)) {
            throw new AlreadyExistException("Event rejected");
        }
    }

    /**
     * Updates the details of an event based on the provided request.
     *
     * @param request the request containing updated event details
     * @param event   the event entity to update
     */
    private void updateEventDetails(EventRequest request, EventEntity event) {
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
        if (request.getStateAction() != null) {
            handleStateAction(request.getStateAction(), event);
        }
    }

    /**
     * Handles the state action for an event, such as publishing or rejecting it.
     *
     * @param stateAction the state action to perform
     * @param event       the event entity to update
     * @throws IllegalArgumentException if the state action is invalid for the current state
     */
    private void handleStateAction(
            final EventStatus stateAction, final EventEntity event) {
        switch (stateAction) {
            case PUBLISH_EVENT:
                if (!event.getState().equals(EventStatus.PENDING)) {
                    log.info("Invalid state action: {}", stateAction);
                    throw new IllegalArgumentException(
                            "Invalid state action: " + stateAction);
                }
                event.setState(EventStatus.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case REJECT_EVENT:
                if (event.getState().equals(EventStatus.PUBLISHED)) {
                    log.info("Invalid state action: {}", stateAction);
                    throw new IllegalArgumentException(
                            "Invalid state action: " + stateAction);
                }
                event.setState(EventStatus.REJECTED);
                break;
            default:
                throw new IllegalArgumentException(
                        "Unhandled state action: " + stateAction);
        }
    }

    /**
     * Builds the specification for filtering events based on the search criteria.
     *
     * @param criteria the search criteria
     * @return the specification for filtering events
     */
    private Specification<EventEntity> buildSpecification(EventSearchCriteriaForAdmin criteria) {
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
        return spec;
    }
}
