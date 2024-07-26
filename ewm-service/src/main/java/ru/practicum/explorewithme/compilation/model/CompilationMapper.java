package ru.practicum.explorewithme.compilation.model;

import ru.practicum.explorewithme.compilation.model.CompilationEntity;
import ru.practicum.explorewithme.compilation.model.CompilationRequest;
import ru.practicum.explorewithme.compilation.model.CompilationResponse;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between CompilationEntity and DTOs.
 */
public class CompilationMapper {
    protected CompilationMapper() {
    }

    /**
     * Converts a CompilationEntity to a CompilationResponse.
     *
     * @param entity the compilation entity to convert
     * @return the compilation response
     */
    public static CompilationResponse toResponse(final CompilationEntity entity) {
        CompilationResponse compilationResponse = CompilationResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .pinned(entity.getPinned())
                .build();
        if (!entity.getEvents().isEmpty()) {
            compilationResponse.getEvents()
                    .addAll((entity.getEvents().stream()
                            .map(EventMapper::toResponse)
                            .collect(Collectors.toList())));
        }
        return compilationResponse;
    }

    /**
     * Converts a CompilationRequest to a CompilationEntity.
     *
     * @param request the compilation request to convert
     * @param events  the list of event entities for the compilation
     * @return the compilation entity
     */
    public static CompilationEntity toEntity(
            final CompilationRequest request,
            final List<EventEntity> events) {
        CompilationEntity compilationEntity = CompilationEntity.builder()
                .title(request.getTitle())
                .pinned(request.getPinned())
                .build();

        compilationEntity.getEvents().addAll(events);
        return compilationEntity;
    }
}
