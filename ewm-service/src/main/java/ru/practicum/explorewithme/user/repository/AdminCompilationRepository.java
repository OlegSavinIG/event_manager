package ru.practicum.explorewithme.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.compilation.model.CompilationEntity;

/**
 * Repository interface for accessing CompilationEntity data with administrative privileges.
 * Extends JpaRepository for basic CRUD operations and query capabilities.
 */
public interface AdminCompilationRepository extends JpaRepository<CompilationEntity, Integer> {
}
