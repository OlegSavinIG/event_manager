package ru.practicum.explorewithme.user.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.model.UserRequest;
import ru.practicum.explorewithme.user.model.UserResponse;
import ru.practicum.explorewithme.user.model.mapper.UserMapper;
import ru.practicum.explorewithme.user.repository.AdminUserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {
    private final AdminUserRepository repository;

    @Override
    @Transactional
    public UserResponse addNewUser(UserRequest userRequest) {
        log.info("Adding new user with request: {}", userRequest);
        UserEntity savedEntity = repository.save(UserMapper.toEntity(userRequest));
        log.info("User added with ID: {}", savedEntity.getId());
        return UserMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserResponse> findByIds(List<Long> ids, int from, int size) {
        log.info("Finding users by IDs: {}, from: {}, size: {}", ids, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        Page<UserEntity> userEntities = repository.findByIdIn(ids, pageable);
        Collection<UserResponse> response = userEntities.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} users", response.size());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserResponse> findAll(int from, int size) {
        log.info("Finding all users from: {}, size: {}", from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        Page<UserEntity> userEntities = repository.findAll(pageable);
        Collection<UserResponse> response = userEntities.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} users", response.size());
        return response;
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        boolean existsById = repository.existsById(userId);
        if (!existsById) {
            log.error("User with ID {} does not exist", userId);
            throw new NotExistException("User not exist");
        }
        repository.deleteById(userId);
        log.info("User with ID {} deleted", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long userId) {
        log.info("Finding user by ID: {}", userId);
        UserEntity userEntity = repository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID {} does not exist", userId);
                    return new NotExistException("User does not exist");
                });
        log.info("Found user with ID: {}", userId);
        return UserMapper.toResponse(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findUserEntity(Long userId) {
        log.info("Finding user entity by ID: {}", userId);
        return repository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID {} does not exist", userId);
                    return new NotExistException("User does not exist");
                });
    }
}
