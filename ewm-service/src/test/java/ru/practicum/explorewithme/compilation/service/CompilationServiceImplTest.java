package ru.practicum.explorewithme.compilation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.compilation.model.CompilationEntity;
import ru.practicum.explorewithme.compilation.model.CompilationMapper;
import ru.practicum.explorewithme.compilation.model.CompilationResponse;
import ru.practicum.explorewithme.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.model.EventResponse;
import ru.practicum.explorewithme.exception.NotExistException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link CompilationServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class CompilationServiceImplTest {
    /**
     * Sets up test data before each test.
     */
    @Mock
    private CompilationRepository repository;
    /**
     * Sets up test data before each test.
     */
    @Mock
    private CompilationMapper mapper;
    /**
     * Sets up test data before each test.
     */
    @InjectMocks
    private CompilationServiceImpl service;
    /**
     * Sets up test data before each test.
     */
    private CompilationEntity compilationEntity;
    /**
     * Sets up test data before each test.
     */
    private CompilationResponse compilationResponse;
    /**
     * Sets up test data before each test.
     */
    private EventEntity eventEntity;
    /**
     * Sets up test data before each test.
     */
    private EventResponse eventResponse;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    void setUp() {
        eventEntity = EventEntity.builder()
                .id(1L)
                .title("Test Event")
                .build();

        eventResponse = EventResponse.builder()
                .id(1L)
                .title("Test Event")
                .build();

        compilationEntity = CompilationEntity.builder()
                .id(1)
                .title("Test Compilation")
                .pinned(true)
                .events(Collections.singletonList(eventEntity))
                .build();

        compilationResponse = CompilationResponse.builder()
                .id(1)
                .title("Test Compilation")
                .pinned(true)
                .events(Collections.singletonList(eventResponse))
                .build();
    }

    /**
     * Tests the getCompilations method.
     */
    @Test
    void getCompilations() {
        when(repository.findAllByPinned(anyBoolean(), any(Pageable.class)))
                .thenReturn(Collections.singletonList(compilationEntity));
        when(mapper.toResponse(any(CompilationEntity.class)))
                .thenReturn(compilationResponse);

        List<CompilationResponse> responses = service
                .getCompilations(true, 0, 10);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(compilationResponse, responses.get(0));

        verify(repository, times(1))
                .findAllByPinned(anyBoolean(), any(Pageable.class));
        verify(mapper, times(1))
                .toResponse(any(CompilationEntity.class));
    }

    /**
     * Tests the getCompilation method for an existing compilation.
     */
    @Test
    void getCompilation() {
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(compilationEntity));
        when(mapper.toResponse(any(CompilationEntity.class)))
                .thenReturn(compilationResponse);

        CompilationResponse response = service.getCompilation(1);

        assertNotNull(response);
        assertEquals(compilationResponse, response);

        verify(repository, times(1))
                .findById(anyInt());
        verify(mapper, times(1))
                .toResponse(any(CompilationEntity.class));
    }

    /**
     * Tests the getCompilation method for a non-existing compilation.
     */
    @Test
    void getCompilation_NotExist() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class,
                () -> service.getCompilation(1));
        assertEquals("Compilation doesn't exist", exception.getMessage());

        verify(repository, times(1)).findById(anyInt());
        verify(mapper, times(0))
                .toResponse(any(CompilationEntity.class));
    }
}
