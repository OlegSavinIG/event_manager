package ru.practicum.explorewithme.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.user.request.model.UserEventRequestEntity;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<UserEventRequestEntity, Long> {
    Optional<List<UserEventRequestEntity>> findAllByEventId(Long eventId);

    List<UserEventRequestEntity> findAllByRequesterId(Long userId);

    Optional<UserEventRequestEntity> findByIdAndRequesterId(Long requestId, Long userId);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);
}
