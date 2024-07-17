package ru.practicum.explorewithme.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.compilation.model.CompilationEntity;

public interface AdminCompilationRepository extends JpaRepository<CompilationEntity, Integer> {
}
