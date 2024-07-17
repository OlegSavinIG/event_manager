package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventResponseShort;
import ru.practicum.explorewithme.event.model.EventSearchCriteria;

import java.util.List;

public interface EventService {
    List<EventResponseShort> getEvents(EventSearchCriteria criteria, Integer from, Integer size);

    EventResponse getEvent(Long id);

    EventEntity getEventEntity(Long id);

    List<EventResponse> getEventsByIds(List<Long> ids);

    List<EventEntity> getEventEntities(List<Long> ids);
}
