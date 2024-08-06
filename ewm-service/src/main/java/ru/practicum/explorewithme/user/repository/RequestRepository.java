package ru.practicum.explorewithme.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.user.request.model.UserEventRequestEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing UserEventRequestEntity data.
 * Provides methods to query and manipulate user event request data.
 */
public interface RequestRepository
        extends JpaRepository<UserEventRequestEntity, Long> {

    /**
     * Finds all user event requests associated with a specific event ID.
     *
     * @param eventId the ID of the event
     * @return a list of user event request entities
     */
    Optional<List<UserEventRequestEntity>> findAllByEventId(Long eventId);

    /**
     * Finds all user event requests initiated by a specific user.
     *
     * @param userId the ID of the requester user
     * @return a list of user event request entities
     */
    List<UserEventRequestEntity> findAllByRequesterId(Long userId);

    /**
     * Finds a user event request by its ID and the ID of the requester user.
     *
     * @param requestId the ID of the request
     * @param userId    the ID of the requester user
     * @return an optional containing the user event request entity, if found
     */
    Optional<UserEventRequestEntity> findByIdAndRequesterId(
            Long requestId, Long userId);

    /**
     * Checks if a user event request exists for a specific user and event.
     *
     * @param userId  the ID of the requester user
     * @param eventId the ID of the event
     * @return true if the request exists, false otherwise
     */
    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);
}
