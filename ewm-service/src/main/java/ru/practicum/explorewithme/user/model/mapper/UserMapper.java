package ru.practicum.explorewithme.user.model.mapper;

import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.model.UserRequest;
import ru.practicum.explorewithme.user.model.UserResponse;
import ru.practicum.explorewithme.user.model.UserResponseWithEvent;

/**
 * Utility class for mapping user-related models.
 */
public class UserMapper {
    protected UserMapper() {
    }

    /**
     * Converts a UserRequest object to a UserEntity object.
     *
     * @param userRequest the UserRequest object to convert
     * @return the corresponding UserEntity object
     */
    public static UserEntity toEntity(final UserRequest userRequest) {
        return UserEntity.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .build();
    }

    /**
     * Converts a UserEntity object to a UserResponse object.
     *
     * @param userEntity the UserEntity object to convert
     * @return the corresponding UserResponse object
     */
    public static UserResponse toResponse(final UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .build();
    }

    /**
     * Converts a UserEntity object to a UserResponseWithEvent object.
     *
     * @param userEntity the UserEntity object to convert
     * @return the corresponding UserResponseWithEvent object
     */
    public static UserResponseWithEvent toResponseWithEvent(
            final UserEntity userEntity) {
        return UserResponseWithEvent.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .build();
    }
}
