package ru.practicum.explorewithme.user.service.privateuser;

import ru.practicum.explorewithme.user.request.model.ApproveRequestCriteria;
import ru.practicum.explorewithme.user.request.model.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.user.request.model.UserEventRequestDto;

import java.util.List;

public interface PrivateUserRequestService {
    List<UserEventRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult approveRequests(Long userId, Long eventId, ApproveRequestCriteria criteria);

    List<UserEventRequestDto> getUserRequests(Long userId);

    UserEventRequestDto createRequest(Long userId, Long eventId);

    UserEventRequestDto cancelRequest(Long userId, Long requestId);
}
