package ru.practicum.explorewithme.user.service.admin;

import ru.practicum.explorewithme.compilation.model.CompilationRequest;
import ru.practicum.explorewithme.compilation.model.CompilationResponse;

/**
 * Service interface for managing compilations by administrators.
 */
public interface AdminCompilationService {

    /**
     * Creates a new compilation.
     *
     * @param compilation the compilation request containing details
     * @return the created compilation response
     */
    CompilationResponse createCompilation(final CompilationRequest compilation);

    /**
     * Deletes a compilation by its ID.
     *
     * @param compId the ID of the compilation to delete
     */
    void deleteCompilationById(final Integer compId);

    /**
     * Updates an existing compilation identified by its ID.
     *
     * @param compilation the updated compilation request
     * @param compId      the ID of the compilation to update
     * @return the updated compilation response
     */
    CompilationResponse updateCompilation(final CompilationRequest compilation,
                                          final Integer compId);
}
