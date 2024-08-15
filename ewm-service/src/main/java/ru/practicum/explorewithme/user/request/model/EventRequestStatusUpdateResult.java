package ru.practicum.explorewithme.user.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Result object representing the outcome of updating event request statuses.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {

    /**
     * List of user event request DTOs for requests that were confirmed.
     */
    @Builder.Default
    private List<UserEventRequestDto> confirmedRequests = new ArrayList<>();

    /**
     * List of user event request DTOs for requests that were rejected.
     */
    @Builder.Default
    private List<UserEventRequestDto> rejectedRequests = new ArrayList<>();
}
