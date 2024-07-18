package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.client.EventClient;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.event.model.mapper.EventMapper;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.specification.EventSpecification;
import ru.practicum.explorewithme.exception.NotExistException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link EventService} interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final EventClient eventClient;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponseShort> getEvents(EventSearchCriteria criteria,
                                              Integer from, Integer size) {
        log.info("Fetching events with criteria: {}, from: {}, size: {}",
                criteria, from, size);
        Specification<EventEntity> spec = Specification.where(null);

        if (criteria.getCategories() != null &&
                !criteria.getCategories().isEmpty()) {
            spec = spec.and(EventSpecification.hasCategories(
                    criteria.getCategories()));
        }
        spec = spec.and(EventSpecification.dateAfter(criteria.getRangeStart()));
        spec = spec.and(EventSpecification.dateBefore(criteria.getRangeEnd()));
        if (criteria.getText() != null && !criteria.getText().isEmpty()) {
            spec = spec.and(EventSpecification.containsText(criteria.getText()));
        }
        if (Boolean.TRUE.equals(criteria.getOnlyAvailable())) {
            spec = spec.and(EventSpecification.isAvailable());
        }
        if (criteria.getPaid() != null) {
            spec = spec.and(EventSpecification.isPaid(criteria.getPaid()));
        }
        spec = spec.and(EventSpecification.excludeStatuses(EventStatus.WAITING,
                EventStatus.REJECTED));

        Sort sort = Sort.unsorted();
        if ("EVENT_DATE".equalsIgnoreCase(criteria.getSort())) {
            sort = Sort.by(Sort.Direction.ASC, "eventDate");
        }
        if ("VIEWS".equalsIgnoreCase(criteria.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "views");
        }

        Pageable pageable = PageRequest.of(from / size, size, sort);

        Page<EventEntity> eventEntities = repository.findAll(spec, pageable);

        List<CompletableFuture<EventEntity>> futures = setEventsViews(
                eventEntities);

        List<EventResponseShort> responses = futures.stream()
                .map(CompletableFuture::join)
                .map(EventMapper::toResponseShort)
                .collect(Collectors.toList());
        log.info("Found {} events with criteria: {}, from: {}, size: {}",
                responses.size(), criteria, from, size);
        return responses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EventResponse getEvent(Long id) {
        log.info("Fetching event with ID: {}", id);
        EventEntity eventEntity = repository.findById(id)
                .orElseThrow(() -> new NotExistException(
                        "This event does not exist"));
        log.info("Found event with ID: {}", id);
        CompletableFuture<Integer> eventViews = eventClient.getEventViews(id);
        try {
            Integer views = eventViews.get();
            eventEntity.setViews(views);
        } catch (InterruptedException | ExecutionException e) {
            log.info("Error fetching event views", e);
            throw new RuntimeException(e);
        }
        return EventMapper.toResponse(eventEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> getEventsByIds(List<Long> ids) {
        log.info("Fetching events with IDs: {}", ids);
        List<EventEntity> eventEntities = repository.findAllById(ids);
        if (eventEntities.isEmpty()) {
            log.warn("No events found with IDs: {}", ids);
            return Collections.emptyList();
        }
        List<EventResponse> responses = eventEntities.stream()
                .map(EventMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} events with IDs: {}", responses.size(), ids);
        return responses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EventEntity getEventEntity(Long id) {
        log.info("Fetching event entity with ID: {}", id);
        EventEntity eventEntity = repository.findById(id)
                .orElseThrow(() -> new NotExistException(
                        "Event does not exist"));
        log.info("Found event entity with ID: {}", id);
        return eventEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public List<EventEntity> getEventEntities(List<Long> ids) {
        log.info("Fetching event entities with IDs: {}", ids);
        List<EventEntity> eventEntities = repository.findAllById(ids);
        log.info("Found {} event entities with IDs: {}", eventEntities.size(),
                ids);
        return eventEntities;
    }

    /**
     * Sets the views for a list of event entities asynchronously.
     *
     * @param eventEntities the list of event entities
     * @return a list of CompletableFutures for the event entities
     */
    private List<CompletableFuture<EventEntity>> setEventsViews(
            Page<EventEntity> eventEntities) {
        int numCores = Runtime.getRuntime().availableProcessors();
        int numThreads = numCores * 2;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        return eventEntities.stream()
                .map(event -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Integer views = eventClient.getEventViews(
                                event.getId()).get();
                        event.setViews(views);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                    return event;
                }, executor))
                .collect(Collectors.toList());
    }
}
