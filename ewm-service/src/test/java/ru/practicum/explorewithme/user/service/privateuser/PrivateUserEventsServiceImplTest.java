package ru.practicum.explorewithme.user.service.privateuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.CategoryResponse;
import ru.practicum.explorewithme.category.service.CategoryService;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.exists.ExistChecker;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.service.admin.AdminUserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrivateUserEventsServiceImplTest {

    @Mock
    private EventRepository repository;

    @Mock
    private AdminUserService adminUserService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ExistChecker checker;

    @InjectMocks
    private PrivateUserEventsServiceImpl service;

    private EventRequest eventRequest;
    private EventEntity eventEntity;
    private EventResponse eventResponse;
    private UserEntity userEntity;
    private CategoryResponse categoryResponse;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder().id(1L).name("Test User").build();
        categoryEntity = CategoryEntity.builder().id(1).name("Test Category").build();
        categoryResponse = CategoryResponse.builder().id(1).name("Test Category").build();

        eventRequest = EventRequest.builder()
                .title("Test Title")
                .annotation("Test Annotation")
                .description("Test Description")
                .eventDate(LocalDateTime.now().plusDays(1))
                .paid(true)
                .participantLimit(100)
                .requestModeration(true)
                .category(1)
                .stateAction("PENDING")
                .build();

        eventEntity = EventEntity.builder()
                .id(1L)
                .title("Test Title")
                .annotation("Test Annotation")
                .description("Test Description")
                .eventDate(LocalDateTime.now().plusDays(1))
                .paid(true)
                .participantLimit(100)
                .requestModeration(true)
                .state(EventStatus.PENDING)
                .category(categoryEntity)
                .initiator(userEntity)
                .build();

        eventResponse = EventResponse.builder()
                .id(1L)
                .title("Test Title")
                .annotation("Test Annotation")
                .description("Test Description")
                .eventDate(LocalDateTime.now().plusDays(1))
                .paid(true)
                .participantLimit(100)
                .requestModeration(true)
                .state(EventStatus.PENDING)
                .category(categoryResponse)
                .build();
    }

    @Test
    void testGetEventsByUserId() {
        Page<EventEntity> page = new PageImpl<>(Collections.singletonList(eventEntity));
        when(repository.findAllByInitiatorId(anyLong(), any(PageRequest.class))).thenReturn(Optional.of(page));

        List<EventResponse> responses = service.getEventsByUserId(1L, 0, 10);

        verify(repository, times(1)).findAllByInitiatorId(anyLong(), any(PageRequest.class));
        assert responses.size() == 1;
        assert responses.get(0).getId().equals(eventEntity.getId());
    }

    @Test
    void testGetByUserIdAndEventId() {
        when(repository.findByIdAndInitiatorId(anyLong(), anyLong())).thenReturn(Optional.of(eventEntity));

        EventResponse response = service.getByUserIdAndEventId(1L, 1L);

        verify(repository, times(1)).findByIdAndInitiatorId(anyLong(), anyLong());
        assert response.getId().equals(eventEntity.getId());
    }

    @Test
    void testCreateEvent() {
        when(adminUserService.findUserEntity(anyLong())).thenReturn(userEntity);
        when(categoryService.getCategory(anyInt())).thenReturn(categoryResponse);
        when(repository.save(any(EventEntity.class))).thenReturn(eventEntity);

        EventResponse response = service.createEvent(eventRequest, 1L);

        verify(repository, times(1)).save(any(EventEntity.class));
        assert response.getId().equals(eventEntity.getId());
    }

    @Test
    void testUpdateEvent() {
        when(repository.findByIdAndInitiatorId(anyLong(), anyLong())).thenReturn(Optional.of(eventEntity));
        when(repository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(categoryService.getCategoryEntity(anyInt())).thenReturn(categoryEntity);

        EventResponse response = service.updateEvent(1L, 1L, eventRequest);

        verify(repository, times(1)).findByIdAndInitiatorId(anyLong(), anyLong());
        verify(repository, times(1)).save(any(EventEntity.class));
        assert response.getId().equals(eventEntity.getId());
    }

    @Test
    void testUpdateEvent_NotExist() {
        when(repository.findByIdAndInitiatorId(anyLong(), anyLong())).thenReturn(Optional.empty());

        try {
            service.updateEvent(1L, 1L, eventRequest);
        } catch (NotExistException e) {
            assert e.getMessage().equals("This event does not exist");
        }

        verify(repository, times(1)).findByIdAndInitiatorId(anyLong(), anyLong());
        verify(repository, times(0)).save(any(EventEntity.class));
    }
}
