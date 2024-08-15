package ru.practicum.explorewithme.event.model.mapper;

import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.mapper.CategoryMapper;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventResponseShort;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.model.mapper.UserMapper;

import java.util.Optional;

/**
 * Mapper class for converting between EventEntity and DTOs.
 */
public class EventMapper {
    protected EventMapper() {
    }

    /**
     * Converts an EventEntity to an EventResponse.
     *
     * @param entity the event entity to convert
     * @return the event response
     */
    public static EventResponse toResponse(final EventEntity entity) {
        return EventResponse.builder()
                .id(entity.getId())
                .annotation(entity.getAnnotation())
                .description(entity.getDescription())
                .category(CategoryMapper.toResponse(entity.getCategory()))
                .eventDate(entity.getEventDate())
                .confirmedRequests(entity.getConfirmedRequests())
                .createdOn(entity.getCreatedOn())
                .state(entity.getState())
                .title(entity.getTitle())
                .views(entity.getViews())
                .paid(Optional.ofNullable(entity.getPaid()).orElse(true))
                .initiator(UserMapper.toResponseWithEvent(entity.getInitiator()))
                .participantLimit(Optional.ofNullable(entity.getParticipantLimit()).orElse(0))
                .publishedOn(entity.getPublishedOn())
                .requestModeration(Optional.ofNullable(entity.getRequestModeration()).orElse(true))
                .build();
    }

    /**
     * Converts an EventEntity to an EventResponseShort.
     *
     * @param entity the event entity to convert
     * @return the short event response
     */
    public static EventResponseShort toResponseShort(final EventEntity entity) {
        return EventResponseShort.builder()
                .id(entity.getId())
                .annotation(entity.getAnnotation())
                .category(CategoryMapper.toResponse(entity.getCategory()))
                .eventDate(entity.getEventDate())
                .confirmedRequests(entity.getConfirmedRequests())
                .title(entity.getTitle())
                .views(entity.getViews())
                .paid(Optional.ofNullable(entity.getPaid()).orElse(false))
                .initiator(UserMapper.toResponseWithEvent(entity.getInitiator()))
                .build();
    }

    /**
     * Converts an EventRequest to an EventEntity.
     *
     * @param request  the event request to convert
     * @param category the category entity for the event
     * @param user     the user entity who initiates the event
     * @return the event entity
     */
    public static EventEntity toEntity(
            final EventRequest request,
            final CategoryEntity category,
            final UserEntity user) {
        return EventEntity.builder()
                .annotation(request.getAnnotation())
                .category(category)
                .paid(request.getPaid())
                .state(request.getStateAction())
                .title(request.getTitle())
                .eventDate(request.getEventDate())
                .initiator(user)
                .description(request.getDescription())
                .participantLimit(request.getParticipantLimit())
                .requestModeration(request.getRequestModeration())
                .build();
    }
}
