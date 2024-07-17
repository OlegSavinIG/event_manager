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

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationController {
    private final CompilationService service;

    @GetMapping
    public ResponseEntity<List<CompilationResponse>> getCompilations(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Received request to get compilations with pinned={}, from={}, size={}", pinned, from, size);
        List<CompilationResponse> compilations = service.getCompilations(pinned, from, size);
        log.info("Returning {} compilations", compilations.size());
        return ResponseEntity.ok(compilations);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationResponse> getCompilation(@PathVariable Integer compId) {
        log.info("Received request to get compilation with ID {}", compId);
        CompilationResponse compilation = service.getCompilation(compId);
        log.info("Returning compilation: {}", compilation);
        return ResponseEntity.ok(compilation);
    }
}
