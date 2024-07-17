package ru.practicum.explorewithme.user.request.model;

public class UserEvenRequestMapper {
    public static UserEventRequestDto toDto(UserEventRequestEntity entity) {
        return UserEventRequestDto.builder()
                .id(entity.getId())
                .requester(entity.getRequester().getId())
                .created(entity.getCreated())
                .event(entity.getEvent().getId())
                .status(entity.getStatus())
                .build();
    }
}
