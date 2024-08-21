package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventStatus;

import java.util.Optional;

/**
 * Repository interface for managing {@link EventEntity} entities.
 */
public interface EventRepository extends JpaRepository<EventEntity, Long>,
        JpaSpecificationExecutor<EventEntity> {

    /**
     * Finds all events by the initiator's ID with pagination.
     *
     * @param userId   the ID of the initiator
     * @param pageable the pagination information
     * @return a page of event entities
     */
    Optional<Page<EventEntity>> findAllByInitiatorId(Long userId,
                                                     Pageable pageable);

    /**
     * Finds an event by its ID and the initiator's ID.
     *
     * @param eventId the ID of the event
     * @param userId  the ID of the initiator
     * @return the event entity
     */
    Optional<EventEntity> findByIdAndInitiatorId(Long eventId, Long userId);

    /**
     * Finds an event by its ID and status.
     *
     * @param eventId the ID of the event
     * @param state   the status of the event
     * @return an optional containing the event entity if found
     */
    Optional<EventEntity> findByIdAndState(Long eventId, EventStatus state);

    /**
     * Checks if a user event request exists for a specific user and event.
     *
     * @param userId  the ID of the initiator user
     * @param eventId the ID of the event
     * @return true if the request exists, false otherwise
     */
    boolean existsByIdAndInitiatorId(Long userId, Long eventId);

    /**
     * Checks if a user event request exists for a specific user and event.
     *
     * @param catId the ID of the event
     * @return true if the request exists, false otherwise
     */
    boolean existsByCategoryId(Integer catId);
}
