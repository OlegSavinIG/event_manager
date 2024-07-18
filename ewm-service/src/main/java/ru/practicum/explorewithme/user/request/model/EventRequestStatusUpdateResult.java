package ru.practicum.explorewithme.user.request.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Result object representing the outcome of updating event request statuses.
 */
@Getter
@Setter
public class EventRequestStatusUpdateResult {

    /**
     * List of user event request DTOs for requests that were confirmed.
     */
    private List<UserEventRequestDto> confirmedRequests;

    /**
     * List of user event request DTOs for requests that were rejected.
     */
    private List<UserEventRequestDto> rejectedRequests;
}
