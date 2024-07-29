package ru.practicum.explorewithme.user.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.exists.ExistChecker;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.model.UserRequest;
import ru.practicum.explorewithme.user.model.UserResponse;
import ru.practicum.explorewithme.user.repository.AdminUserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AdminUserServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class AdminUserServiceImplTest {
    /**
     * Sets up test data before each test.
     */
    @Mock
    private AdminUserRepository repository;
    /**
     * Sets up test data before each test.
     */
    @Mock
    private ExistChecker checker;
    /**
     * Sets up test data before each test.
     */
    @InjectMocks
    private AdminUserServiceImpl service;
    /**
     * Sets up test data before each test.
     */
    private UserRequest userRequest;
    /**
     * Sets up test data before each test.
     */
    private UserEntity userEntity;
    /**
     * Sets up test data before each test.
     */
    private UserResponse userResponse;
    /**
     * Sets up test data before each test.
     */
   private final int pageSize = 10;
    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    void setUp() {
        userRequest = UserRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .build();

        userEntity = UserEntity.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    /**
     * Tests the addNewUser method.
     */
    @Test
    void testAddNewUser() {
        when(repository.save(any(UserEntity.class)))
                .thenReturn(userEntity);
       doNothing().when(checker).isUserExistsByEmail(any(String.class));

        UserResponse response = service.addNewUser(userRequest);

        verify(repository, times(1))
                .save(any(UserEntity.class));
        assert response.getId().equals(userEntity.getId());
    }

    /**
     * Tests the findByIds method.
     */
    @Test
    void testFindByIds() {
        Page<UserEntity> page = new PageImpl<>(Collections
                .singletonList(userEntity));
        when(repository.findByIdIn(anyList(),
                any(PageRequest.class))).thenReturn(page);

        List<Long> ids = Collections.singletonList(1L);
        Collection<UserResponse> responses = service
                .findByIds(ids, 0, pageSize);

        verify(repository, times(1))
                .findByIdIn(anyList(), any(PageRequest.class));
        assert responses.size() == 1;
        assert responses.iterator().next().getId().equals(userEntity.getId());
    }

    /**
     * Tests the findAll method.
     */
    @Test
    void testFindAll() {
        Page<UserEntity> page = new PageImpl<>(Collections
                .singletonList(userEntity));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Collection<UserResponse> responses = service.findAll(0, pageSize);

        verify(repository, times(1))
                .findAll(any(PageRequest.class));
        assert responses.size() == 1;
        assert responses.iterator().next().getId().equals(userEntity.getId());
    }

    /**
     * Tests the deleteUserById method.
     */
    @Test
    void testDeleteUserById() {
        when(repository.existsById(anyLong())).thenReturn(true);

        service.deleteUserById(1L);

        verify(repository, times(1))
                .existsById(anyLong());
        verify(repository, times(1))
                .deleteById(anyLong());
    }

    /**
     * Tests the deleteUserById method for a non-existing user.
     */
    @Test
    void testDeleteUserByIdNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);

        try {
            service.deleteUserById(1L);
        } catch (NotExistException e) {
            assert e.getMessage().equals("User not exist");
        }

        verify(repository, times(1))
                .existsById(anyLong());
        verify(repository, times(0))
                .deleteById(anyLong());
    }

    /**
     * Tests the findById method.
     */
    @Test
    void testFindById() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(userEntity));

        UserResponse response = service.findById(1L);

        verify(repository, times(1))
                .findById(anyLong());
        assert response.getId().equals(userEntity.getId());
    }

    /**
     * Tests the findById method for a non-existing user.
     */
    @Test
    void testFindByIdNotExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            service.findById(1L);
        } catch (NotExistException e) {
            assert e.getMessage().equals("User does not exist");
        }

        verify(repository, times(1)).findById(anyLong());
    }

    /**
     * Tests the findUserEntity method.
     */
    @Test
    void testFindUserEntity() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(userEntity));

        UserEntity response = service.findUserEntity(1L);

        verify(repository, times(1))
                .findById(anyLong());
        assert response.getId().equals(userEntity.getId());
    }

    /**
     * Tests the findUserEntity method for a non-existing user.
     */
    @Test
    void testFindUserEntityNotExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            service.findUserEntity(1L);
        } catch (NotExistException e) {
            assert e.getMessage().equals("User does not exist");
        }

        verify(repository, times(1)).findById(anyLong());
    }
}
