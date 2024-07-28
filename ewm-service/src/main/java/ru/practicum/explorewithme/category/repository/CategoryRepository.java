package ru.practicum.explorewithme.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.category.model.CategoryEntity;

/**
 * Repository interface for managing {@link CategoryEntity} entities.
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity,
        Integer> {
    boolean existsByName(String name);
}
