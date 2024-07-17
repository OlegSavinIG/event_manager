package ru.practicum.explorewithme.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.CompilationRequest;
import ru.practicum.explorewithme.compilation.model.CompilationResponse;
import ru.practicum.explorewithme.user.service.admin.AdminCompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final AdminCompilationService service;

    @PostMapping("/compilations")
    public ResponseEntity<CompilationResponse> createCompilation(
            @Valid @RequestBody CompilationRequest compilation) {
        return ResponseEntity.ok(service.createCompilation(compilation));
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilationById(@PathVariable Integer compId) {
        service.deleteCompilationById(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<CompilationResponse> updateCompilation(
            @Valid @RequestBody CompilationRequest compilation,
            @PathVariable Integer compId
    ) {
        return ResponseEntity.ok(service.updateCompilation(compilation, compId));
    }
}
