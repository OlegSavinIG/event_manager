package ru.practicum.explorewithme.user.service.privateuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventRequest;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exists.ExistChecker;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.model.UserResponseWithEvent;
import ru.practicum.explorewithme.user.repository.RequestRepository;
import ru.practicum.explorewithme.user.request.model.ApproveRequestCriteria;
import ru.practicum.explorewithme.user.request.model.UserEventRequestDto;
import ru.practicum.explorewithme.user.request.model.UserEventRequestEntity;
import ru.practicum.explorewithme.user.service.admin.AdminUserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link PrivateUserRequestServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class PrivateUserRequestServiceImplTest {
    /**
     * Sets up test data before each test.
     */
    @Mock
    private RequestRepository repository;
    /**
     * Sets up test data before each test.
     */
    @Mock
    private EventService eventService;
    /**
     * Sets up test data before each test.
     */
    @Mock
    private EventRepository eventRepository;
    /**
     * Sets up test data before each test.
     */
    @Mock
    private AdminUserService adminUserService;
    /**
     * Sets up test data before each test.
     */
    @Mock
    private ExistChecker checker;
    /**
     * Sets up test data before each test.
     */
    @InjectMocks
    private PrivateUserRequestServiceImpl service;
    /**
     * Sets up test data before each test.
     */
    private UserEntity userEntity;
    /**
     * Sets up test data before each test.
     */
    private EventEntity eventEntity;
    /**
     * Sets up test data before each test.
     */
    private UserEventRequestEntity requestEntity;
    /**
     * Sets up test data before each test.
     */
    private EventRequest eventRequest;
    /**
     * Sets up test data before each test.
     */
    private EventResponse eventResponse;
    /**
     * Sets up test data before each test.
     */
    private ApproveRequestCriteria criteria;


    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .name("Test User")
                .build();
        eventEntity = EventEntity.builder()
                .id(1L).title("Test Event")
                .initiator(userEntity)
                .requestModeration(true)
                .confirmedRequests(0)
                .build();
        requestEntity = UserEventRequestEntity.builder()
                .id(1L).status("PENDING")
                .requester(userEntity)
                .event(eventEntity)
                .created(LocalDateTime.now())
                .build();
        eventRequest = EventRequest.builder()
                .title("Test Event")
                .build();
        eventResponse = EventResponse.builder()
                .title("Test Event")
                .initiator(new UserResponseWithEvent(1L, "name"))
                .build();
        criteria = new ApproveRequestCriteria();
        criteria.setIds(Collections.singletonList(1L));
        criteria.setStatus("CONFIRMED");
    }

    /**
     * Tests the getEventRequests method.
     */
    @Test
    void testGetEventRequests() {
        when(repository.findAllByEventId(anyLong()))
                .thenReturn(Optional.of(Collections.singletonList(requestEntity)));
        when(eventService.getEvent(anyLong())).thenReturn(eventResponse);

        List<UserEventRequestDto> responses = service.getEventRequests(1L, 1L);

        verify(repository, times(1)).findAllByEventId(anyLong());
        assert responses.size() == 1;
        assert responses.get(0).getId().equals(requestEntity.getId());
    }

    /**
     * Tests the approveRequests method.
     */
    @Test
    void testApproveRequests() throws Exception {
        when(eventService.getEventEntity(anyLong())).thenReturn(eventEntity);
        when(repository.findAllById(any()))
                .thenReturn(Collections.singletonList(requestEntity));

        service.approveRequests(1L, 1L, criteria);

        verify(repository, times(1)).findAllById(any());
        verify(repository, times(1)).save(any(UserEventRequestEntity.class));
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    /**
     * Tests the getUserRequests method.
     */
    @Test
    void testGetUserRequests() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.findAllByRequesterId(anyLong()))
                .thenReturn(Collections.singletonList(requestEntity));

        List<UserEventRequestDto> responses = service.getUserRequests(1L);

        verify(repository, times(1)).existsById(anyLong());
        verify(repository, times(1)).findAllByRequesterId(anyLong());
        assert responses.size() == 1;
        assert responses.get(0).getId().equals(requestEntity.getId());
    }

    /**
     * Tests the createRequest method.
     */
    @Test
    void testCreateRequest() {
        when(repository.existsByRequesterIdAndEventId(anyLong(), anyLong()))
                .thenReturn(false);
        when(eventService.getEventEntity(anyLong())).thenReturn(eventEntity);
        when(adminUserService.findUserEntity(anyLong())).thenReturn(userEntity);
        when(repository.save(any(UserEventRequestEntity.class))).thenReturn(requestEntity);

        UserEventRequestDto response = service.createRequest(1L, 1L);

        verify(repository, times(1))
                .existsByRequesterIdAndEventId(anyLong(), anyLong());
        verify(repository, times(1)).save(any(UserEventRequestEntity.class));
        assert response.getId().equals(requestEntity.getId());
    }

    /**
     * Tests the cancelRequest method.
     */
    @Test
    void testCancelRequest() {
        when(repository.findByIdAndRequesterId(anyLong(), anyLong()))
                .thenReturn(Optional.of(requestEntity));

        UserEventRequestDto response = service.cancelRequest(1L, 1L);

        verify(repository, times(1))
                .findByIdAndRequesterId(anyLong(), anyLong());
        verify(repository, times(1)).delete(any(UserEventRequestEntity.class));
        assert response.getId().equals(requestEntity.getId());
    }
}
