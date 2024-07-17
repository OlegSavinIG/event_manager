package ru.practicum.explorewithme.user.service.admin;

import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.model.UserRequest;
import ru.practicum.explorewithme.user.model.UserResponse;

import java.util.Collection;
import java.util.List;

public interface AdminUserService {
    UserResponse addNewUser(UserRequest userRequest);

    Collection<UserResponse> findByIds(List<Long> ids, int from, int size);

    Collection<UserResponse> findAll(int from, int size);

    void deleteUserById(Long userId);

    UserResponse findById(Long userId);

    UserEntity findUserEntity(Long userId);
}
