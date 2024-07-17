package ru.practicum.explorewithme.user.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.model.UserRequest;
import ru.practicum.explorewithme.user.model.UserResponse;
import ru.practicum.explorewithme.user.model.UserResponseWithEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserEntity toEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .build();
    }

    public static UserResponse toResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .build();
    }

    public static UserResponseWithEvent toResponseWithEvent(
            UserEntity userEntity) {
        return UserResponseWithEvent.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .build();
    }
}
