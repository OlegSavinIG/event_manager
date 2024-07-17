package ru.practicum.explorewithme.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.compilation.model.CompilationEntity;

import java.util.List;

public interface CompilationRepository extends JpaRepository<CompilationEntity, Integer> {
    List<CompilationEntity> findAllByPinned(Boolean pinned, Pageable pageable);
}
