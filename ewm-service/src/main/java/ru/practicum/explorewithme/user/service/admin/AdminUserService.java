package ru.practicum.explorewithme.user.service.admin;

import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.model.UserRequest;
import ru.practicum.explorewithme.user.model.UserResponse;

import java.util.Collection;
import java.util.List;

/**
 * Service interface for admin user operations.
 */
public interface AdminUserService {

    /**
     * Adds a new user.
     *
     * @param userRequest the user request containing user details
     * @return the added user response
     */
    UserResponse addNewUser(UserRequest userRequest);

    /**
     * Finds users by their IDs.
     *
     * @param ids  the list of user IDs
     * @param from the starting index of the result
     * @param size the number of results to retrieve
     * @return the collection of user responses
     */
    Collection<UserResponse> findByIds(List<Long> ids, int from, int size);

    /**
     * Finds all users with pagination.
     *
     * @param from the starting index of the result
     * @param size the number of results to retrieve
     * @return the collection of user responses
     */
    Collection<UserResponse> findAll(int from, int size);

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    void deleteUserById(Long userId);

    /**
     * Finds a user by their ID.
     *
     * @param userId the ID of the user to find
     * @return the found user response
     */
    UserResponse findById(Long userId);

    /**
     * Finds a user entity by their ID.
     *
     * @param userId the ID of the user entity to find
     * @return the found user entity
     */
    UserEntity findUserEntity(Long userId);

    void saveUser(UserEntity entity);
}
