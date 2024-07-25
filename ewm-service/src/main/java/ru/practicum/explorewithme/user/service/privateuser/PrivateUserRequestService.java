package ru.practicum.explorewithme.user.service.privateuser;

import ru.practicum.explorewithme.user.request.model.ApproveRequestCriteria;
import ru.practicum.explorewithme.user.request.model.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.user.request.model.UserEventRequestDto;

import java.util.List;

/**
 * Service interface for managing user requests for events.
 */
public interface PrivateUserRequestService {

    /**
     * Retrieves requests for a specific event by a user.
     *
     * @param userId  the ID of the user
     * @param eventId the ID of the event
     * @return the list of user event request DTOs
     */
    List<UserEventRequestDto> getEventRequests(Long userId, Long eventId);

    /**
     * Approves or rejects user requests for an event based on criteria.
     *
     * @param userId   the ID of the user
     * @param eventId  the ID of the event
     * @param criteria the criteria for approving or rejecting requests
     * @return the result of the request status update
     */
    EventRequestStatusUpdateResult approveRequests(Long userId, Long eventId,
                                                   ApproveRequestCriteria criteria);

    /**
     * Retrieves all requests made by a specific user.
     *
     * @param userId the ID of the user
     * @return the list of user event request DTOs
     */
    List<UserEventRequestDto> getUserRequests(Long userId);

    /**
     * Creates a new request for an event by a user.
     *
     * @param userId  the ID of the user
     * @param eventId the ID of the event
     * @return the created user event request DTO
     */
    UserEventRequestDto createRequest(Long userId, Long eventId);

    /**
     * Cancels a request made by a user.
     *
     * @param userId    the ID of the user
     * @param requestId the ID of the request
     * @return the canceled user event request DTO
     */
    UserEventRequestDto cancelRequest(Long userId, Long requestId);
}
