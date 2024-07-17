package ru.practicum.explorewithme.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.user.model.UserEntity;

import java.util.List;

public interface AdminUserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.id IN :ids")
    Page<UserEntity> findByIdIn(@Param("ids") List<Long> ids, Pageable pageable);
}