package ru.practicum.explorewithme.compilation.model;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.mapper.EventMapper;
import ru.practicum.explorewithme.event.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    @Mapping(target = "events", expression = "java(mapIdsToEvents(request.getEvents(), eventService))")
    CompilationEntity toEntity(CompilationRequest request, @Context EventService eventService);

    @Mapping(target = "events", expression = "java(mapToResponses(entity.getEvents()))")
    CompilationResponse toResponse(CompilationEntity entity);

    default List<EventEntity> mapIdsToEvents(List<Long> ids, @Context EventService eventService) {
        return eventService.getEventEntities(ids);
    }

    default List<EventResponse> mapToResponses(List<EventEntity> events) {
        return events.stream()
                .map(EventMapper::toResponse)
                .collect(Collectors.toList());
    }
}
