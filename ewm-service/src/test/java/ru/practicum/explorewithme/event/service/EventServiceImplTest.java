package ru.practicum.explorewithme.event.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.event.client.EventClient;
import ru.practicum.explorewithme.event.model.*;
import ru.practicum.explorewithme.event.model.mapper.EventMapper;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.user.model.UserEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository repository;

    @Mock
    private EventClient eventClient;

    @InjectMocks
    private EventServiceImpl service;

    private EventEntity eventEntity;
    private EventResponse eventResponse;
    private EventResponseShort eventResponseShort;
    private EventSearchCriteria criteria;
    private CategoryEntity category;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .name("Name User")
                .id(1L)
                .build();
        category = CategoryEntity.builder()
                .id(1)
                .name("Name")
                .build();

        eventEntity = EventEntity.builder()
                .id(1L)
                .title("Test Event")
                .annotation("Test Annotation")
                .description("Test Description")
                .eventDate(LocalDateTime.now().plusDays(1))
                .state(EventStatus.PENDING)
                .category(category)
                .initiator(userEntity)
                .build();

        eventResponse = EventMapper.toResponse(eventEntity);
        eventResponseShort = EventMapper.toResponseShort(eventEntity);
        eventResponseShort.setViews(100);  // Устанавливаем просмотры для теста

        criteria = new EventSearchCriteria();
        criteria.setCategories(Collections.singletonList(1));
        criteria.setRangeStart(LocalDateTime.now().minusDays(1));
        criteria.setRangeEnd(LocalDateTime.now().plusDays(1));
    }

    @Test
    void getEvents() {
        Page<EventEntity> page = new PageImpl<>(Collections.singletonList(eventEntity));
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(eventClient.getEventViews(anyLong())).thenReturn(CompletableFuture.completedFuture(100));

        List<EventResponseShort> responses = service.getEvents(criteria, 0, 10);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(eventResponseShort, responses.get(0));
        assertEquals(100, responses.get(0).getViews());

        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(eventClient, times(1)).getEventViews(anyLong());
    }

    @Test
    void getEvent() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(eventEntity));
        when(eventClient.getEventViews(anyLong())).thenReturn(CompletableFuture.completedFuture(100));

        EventResponse response = service.getEvent(1L);

        assertNotNull(response);
        assertEquals(100, response.getViews());

        verify(repository, times(1)).findById(anyLong());
        verify(eventClient, times(1)).getEventViews(anyLong());
    }

    @Test
    void getEvent_NotExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class, () -> service.getEvent(1L));
        assertEquals("This event does not exist", exception.getMessage());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getEventsByIds() {
        when(repository.findAllById(anyList())).thenReturn(Collections.singletonList(eventEntity));

        List<EventResponse> responses = service.getEventsByIds(Collections.singletonList(1L));

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(eventResponse, responses.get(0));

        verify(repository, times(1)).findAllById(anyList());
    }

    @Test
    void getEventsByIds_Empty() {
        when(repository.findAllById(anyList())).thenReturn(Collections.emptyList());

        List<EventResponse> responses = service.getEventsByIds(Collections.singletonList(1L));

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(repository, times(1)).findAllById(anyList());
    }

    @Test
    void getEventEntity() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(eventEntity));

        EventEntity entity = service.getEventEntity(1L);

        assertNotNull(entity);
        assertEquals(eventEntity, entity);

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getEventEntity_NotExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class, () -> service.getEventEntity(1L));
        assertEquals("Event does not exist", exception.getMessage());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getEventEntities() {
        when(repository.findAllById(anyList())).thenReturn(Collections.singletonList(eventEntity));

        List<EventEntity> entities = service.getEventEntities(Collections.singletonList(1L));

        assertNotNull(entities);
        assertEquals(1, entities.size());
        assertEquals(eventEntity, entities.get(0));

        verify(repository, times(1)).findAllById(anyList());
    }
}
