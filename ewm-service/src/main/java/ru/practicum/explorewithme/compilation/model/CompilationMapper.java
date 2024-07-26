package ru.practicum.explorewithme.compilation.model;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.mapper.EventMapper;
import ru.practicum.explorewithme.event.service.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between CompilationEntity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface CompilationMapper {

    /**
     * Converts a CompilationRequest to a CompilationEntity.
     *
     * @param request      the compilation request to convert
     * @param eventService the event service for fetching event entities
     * @return the compilation entity
     */
//    @Mapping(target = "events",
//            expression =
//                    "java(mapIdsToEvents(request.getEvents(), eventService))")
    @Mapping(target = "events", source = "events",
            qualifiedByName = "mapIdsToEvents")
    CompilationEntity toEntity(CompilationRequest request,
                               @Context EventService eventService);

    /**
     * Converts a CompilationEntity to a CompilationResponse.
     *
     * @param entity the compilation entity to convert
     * @return the compilation response
     */
//    @Mapping(target = "events",
//            expression = "java(mapToResponses(entity.getEvents()))")
    @Mapping(target = "events", source = "events",
            qualifiedByName = "mapToResponses")
    CompilationResponse toResponse(CompilationEntity entity);

    /**
     * Maps a list of event IDs to a list of EventEntity objects.
     *
     * @param ids          the list of event IDs
     * @param eventService the event service for fetching event entities
     * @return the list of event entities
     */
    @Named("mapIdsToEvents")
    default List<EventEntity> mapIdsToEvents(
            List<Long> ids,
            @Context EventService eventService) {
        return eventService.getEventEntities(ids);
    }

    /**
     * Maps a list of EventEntity objects to a list of EventResponse objects.
     *
     * @param events the list of event entities
     * @return the list of event responses
     */
    @Named("mapToResponses")
    default List<EventResponse> mapToResponses(List<EventEntity> events) {
        if (events == null) {
            return new ArrayList<>();
        }
        return events.stream()
                .map(EventMapper::toResponse)
                .collect(Collectors.toList());
    }
}
