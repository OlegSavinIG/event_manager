package ru.practicum.explorewithme.user.service.admin;

import ru.practicum.explorewithme.compilation.model.CompilationRequest;
import ru.practicum.explorewithme.compilation.model.CompilationResponse;

public interface AdminCompilationService {
    CompilationResponse createCompilation(CompilationRequest compilation);

    void deleteCompilationById(Integer compId);

    CompilationResponse updateCompilation(CompilationRequest compilation, Integer compId);
}
