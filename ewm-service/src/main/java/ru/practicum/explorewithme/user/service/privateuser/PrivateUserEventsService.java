package ru.practicum.explorewithme.user.service.privateuser;

import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;

import java.util.List;

public interface PrivateUserEventsService {
    List<EventResponse> getEventsByUserId(Long userId, Integer from, Integer size);

    EventResponse getByUserIdAndEventId(Long userId, Long eventId);

    EventResponse createEvent(EventRequest request, Long userId);

    EventResponse updateEvent(Long userId, Long eventId, EventRequest request);
}
