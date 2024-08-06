package ru.practicum.explorewithme.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.compilation.model.CompilationEntity;

import java.util.List;

/**
 * Repository interface for managing {@link CompilationEntity} entities.
 */
public interface CompilationRepository extends JpaRepository<CompilationEntity,
        Integer> {

    /**
     * Finds all compilations by pinned status with pagination.
     *
     * @param pinned   the pinned status of the compilations
     * @param pageable the pagination information
     * @return the list of compilation entities
     */
    List<CompilationEntity> findAllByPinned(Boolean pinned, Pageable pageable);
}
