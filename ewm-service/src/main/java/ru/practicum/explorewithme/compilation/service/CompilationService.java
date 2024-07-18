package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.model.CompilationResponse;

import java.util.List;

/**
 * Service interface for managing compilations.
 */
public interface CompilationService {

    /**
     * Retrieves a list of compilations based on the pinned status.
     *
     * @param pinned the pinned status of the compilations
     * @param from   the starting index of the result
     * @param size   the number of results to retrieve
     * @return the list of compilation responses
     */
    List<CompilationResponse> getCompilations(Boolean pinned, Integer from,
                                              Integer size);

    /**
     * Retrieves a specific compilation by its ID.
     *
     * @param compId the ID of the compilation
     * @return the compilation response
     */
    CompilationResponse getCompilation(Integer compId);
}
