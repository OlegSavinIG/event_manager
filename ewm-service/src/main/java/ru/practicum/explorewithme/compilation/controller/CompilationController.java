package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.CompilationResponse;
import ru.practicum.explorewithme.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * REST controller for managing compilations.
 */
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationController {

    private final CompilationService service;

    /**
     * Retrieves a list of compilations based on the pinned status.
     *
     * @param pinned the pinned status of the compilations
     * @param from   the starting index of the result
     * @param size   the number of results to retrieve
     * @return a response entity containing the list of compilation responses
     */
    @GetMapping
    public ResponseEntity<List<CompilationResponse>> getCompilations(
            @RequestParam(defaultValue = "false") final Boolean pinned,
            @PositiveOrZero @RequestParam(defaultValue = "0") final Integer from,
            @Positive @RequestParam(defaultValue = "10") final Integer size) {
        log.info("Received request to get compilations with pinned={}, from={}, size={}",
                pinned, from, size);
        List<CompilationResponse> compilations = service.getCompilations(
                pinned, from, size);
        log.info("Returning {} compilations", compilations.size());
        return ResponseEntity.ok(compilations);
    }

    /**
     * Retrieves a specific compilation by its ID.
     *
     * @param compId the ID of the compilation
     * @return a response entity containing the compilation response
     */
    @GetMapping("/{compId}")
    public ResponseEntity<CompilationResponse> getCompilation(
            @PathVariable final Integer compId) {
        log.info("Received request to get compilation with ID {}", compId);
        CompilationResponse compilation = service.getCompilation(compId);
        log.info("Returning compilation: {}", compilation);
        return ResponseEntity.ok(compilation);
    }
}
