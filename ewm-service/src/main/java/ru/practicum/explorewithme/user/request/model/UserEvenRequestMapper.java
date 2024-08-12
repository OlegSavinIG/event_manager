package ru.practicum.explorewithme.user.request.model;

/**
 * Utility class for mapping
 * UserEventRequestEntity objects
 * to UserEventRequestDto objects.
 */
public class UserEvenRequestMapper {
    protected UserEvenRequestMapper() {
    }

    /**
     * Converts a UserEventRequestEntity
     * object to a UserEventRequestDto object.
     *
     * @param entity the UserEventRequestEntity object to convert
     * @return the corresponding UserEventRequestDto object
     */
    public static UserEventRequestDto toDto(
            final UserEventRequestEntity entity) {
        return UserEventRequestDto.builder()
                .id(entity.getId())
                .requester(entity.getRequester().getId())
                .created(entity.getCreated())
                .event(entity.getEvent().getId())
                .status(entity.getStatus().name())
                .build();
    }
}
