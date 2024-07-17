package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.model.CompilationResponse;

import java.util.List;

public interface CompilationService {
    List<CompilationResponse> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationResponse getCompilation(Integer compId);
}
