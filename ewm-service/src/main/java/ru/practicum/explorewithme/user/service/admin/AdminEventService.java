package ru.practicum.explorewithme.user.service.admin;

import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.user.model.EventSearchCriteriaForAdmin;

import java.util.List;

public interface AdminEventService {
    List<EventResponse> getEvents(EventSearchCriteriaForAdmin criteria, Integer from, Integer size);

    EventResponse approveEvent(EventRequest request, Long eventId);
}
