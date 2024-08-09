package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.event.model.EventEntity;

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

    Optional<EventEntity> findByIdAndStatePublished(Long id);
}
