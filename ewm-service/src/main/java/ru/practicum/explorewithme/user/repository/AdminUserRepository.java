package ru.practicum.explorewithme.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.user.model.UserEntity;

import java.util.List;

/**
 * Repository interface for accessing
 * UserEntity data with administrative privileges.
 * Extends JpaRepository for basic CRUD operations.
 */
public interface AdminUserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Custom query to find users by their IDs.
     *
     * @param ids      list of user IDs to search for
     * @param pageable pageable object for pagination
     * @return a page of UserEntity objects that match the IDs
     */
    @Query("SELECT u FROM UserEntity u WHERE u.id IN :ids")
    Page<UserEntity> findByIdIn(
            @Param("ids") List<Long> ids, Pageable pageable);
}
