package ru.practicum.explorewithme.user.request.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateResult {
    private List<UserEventRequestDto> confirmedRequests;
    private List<UserEventRequestDto> rejectedRequests;
}
