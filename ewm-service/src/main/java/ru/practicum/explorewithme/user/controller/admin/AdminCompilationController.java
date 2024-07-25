package ru.practicum.explorewithme.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.CompilationRequest;
import ru.practicum.explorewithme.compilation.model.CompilationResponse;
import ru.practicum.explorewithme.user.service.admin.AdminCompilationService;

import javax.validation.Valid;

/**
 * REST controller for managing compilations by admin.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCompilationController {

    /**
     * REST service for managing compilations by admin.
     */

    private final AdminCompilationService service;

    /**
     * Creates a new compilation.
     *
     * @param compilation the compilation request to create
     * @return the created compilation response
     */
    @PostMapping("/compilations")
    public ResponseEntity<CompilationResponse> createCompilation(
            @Valid @RequestBody CompilationRequest compilation
    ) {
        return ResponseEntity.ok(service.createCompilation(compilation));
    }

    /**
     * Deletes a compilation by its ID.
     *
     * @param compId the ID of the compilation to delete
     */
    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilationById(@PathVariable Integer compId) {
        service.deleteCompilationById(compId);
    }

    /**
     * Updates a compilation by its ID.
     *
     * @param compilation the compilation request with updated information
     * @param compId      the ID of the compilation to update
     * @return the updated compilation response
     */
    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<CompilationResponse> updateCompilation(
            @Valid @RequestBody CompilationRequest compilation,
            @PathVariable Integer compId
    ) {
        return ResponseEntity.ok(service.updateCompilation(compilation, compId));
    }
}
