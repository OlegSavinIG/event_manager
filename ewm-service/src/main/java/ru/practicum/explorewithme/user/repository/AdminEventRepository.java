package ru.practicum.explorewithme.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.event.model.EventEntity;

/**
 * Repository interface for accessing
 * EventEntity data with administrative privileges.
 * Extends JpaRepository for basic
 * CRUD operations and query capabilities.
 * Extends JpaSpecificationExecutor
 * for executing JPA specifications.
 */
public interface AdminEventRepository
        extends JpaRepository<EventEntity, Long>,
        JpaSpecificationExecutor<EventEntity> {
    @EntityGraph(attributePaths = {"category", "initiator", "confirmedRequests"})
    Page<EventEntity> findAll(Specification<EventEntity> spec, Pageable pageable);

}
