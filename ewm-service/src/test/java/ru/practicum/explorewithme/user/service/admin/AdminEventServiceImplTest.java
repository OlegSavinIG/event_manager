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
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.user.model.EventSearchCriteriaForAdmin;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.model.mapper.UserMapper;
import ru.practicum.explorewithme.user.repository.AdminEventRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AdminEventServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class AdminEventServiceImplTest {

    @Mock
    private AdminEventRepository repository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AdminEventServiceImpl service;

    private EventRequest eventRequest;
    private EventEntity eventEntity;
    private EventResponse eventResponse;
    private EventSearchCriteriaForAdmin criteria;
    private CategoryEntity categoryEntity;
    private UserEntity userEntity;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    void setUp() {
        categoryEntity = CategoryEntity.builder().id(1).name("Test Category").build();
        userEntity = UserEntity.builder().id(1L).name("Test User").build();

        eventRequest = EventRequest.builder()
                .annotation("Test Annotation")
                .description("Test Description")
                .title("Test Title")
                .eventDate(LocalDateTime.now().plusDays(1))
                .paid(true)
                .participantLimit(100)
                .requestModeration(true)
                .category(1)
                .stateAction("PUBLISH_EVENT")
                .build();

        eventEntity = EventEntity.builder()
                .id(1L)
                .annotation("Test Annotation")
                .description("Test Description")
                .title("Test Title")
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
                .annotation("Test Annotation")
                .description("Test Description")
                .title("Test Title")
                .eventDate(LocalDateTime.now().plusDays(1))
                .paid(true)
                .participantLimit(100)
                .requestModeration(true)
                .state(EventStatus.PUBLISHED)
                .category(CategoryMapper.toResponse(categoryEntity))
                .initiator(UserMapper.toResponseWithEvent(userEntity))
                .build();

        criteria = new EventSearchCriteriaForAdmin();
        criteria.setUsers(Collections.singletonList(1L));
        criteria.setStates(Collections.singletonList("PENDING"));
        criteria.setCategories(Collections.singletonList(1));
        criteria.setRangeStart(LocalDateTime.now().minusDays(1));
        criteria.setRangeEnd(LocalDateTime.now().plusDays(1));
    }

    /**
     * Tests the getEvents method.
     */
    @Test
    void testGetEvents() {
        Page<EventEntity> page = new PageImpl<>(Collections.singletonList(eventEntity));
        when(repository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(page);

        List<EventResponse> responses = service.getEvents(criteria, 0, 10);

        verify(repository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
        assert responses.size() == 1;
        assert responses.get(0).getId().equals(eventEntity.getId());
    }

    /**
     * Tests the approveEvent method.
     */
    @Test
    void testApproveEvent() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(categoryEntity));

        EventResponse response = service.approveEvent(eventRequest, 1L);

        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
        assert response != null;
        assert response.getId().equals(eventEntity.getId());
    }

    /**
     * Tests the approveEvent method for a non-existing event.
     */
    @Test
    void testApproveEvent_NotExist() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            service.approveEvent(eventRequest, 1L);
        } catch (NotExistException e) {
            assert e.getMessage().equals("Event with id=1 was not found");
        }

        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(0)).save(any(EventEntity.class));
    }

    /**
     * Tests the approveEvent method with an invalid category.
     */
    @Test
    void testApproveEvent_InvalidCategory() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            service.approveEvent(eventRequest, 1L);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Category not found");
        }

        verify(eventRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).findById(anyInt());
        verify(eventRepository, times(0)).save(any(EventEntity.class));
    }
}
