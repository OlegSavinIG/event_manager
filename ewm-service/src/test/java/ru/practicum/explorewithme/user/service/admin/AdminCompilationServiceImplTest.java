package ru.practicum.explorewithme.user.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explorewithme.compilation.model.CompilationEntity;
import ru.practicum.explorewithme.compilation.model.CompilationMapper;
import ru.practicum.explorewithme.compilation.model.CompilationRequest;
import ru.practicum.explorewithme.compilation.model.CompilationResponse;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.exception.NotExistException;
import ru.practicum.explorewithme.user.repository.AdminCompilationRepository;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AdminCompilationServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class AdminCompilationServiceImplTest {
    /**
     * Sets up test data before each test.
     */
    @Mock
    private AdminCompilationRepository repository;
    /**
     * Sets up test data before each test.
     */
    @Mock
    private CompilationMapper mapper;
    /**
     * Sets up test data before each test.
     */
    @Mock
    private EventService eventService;
    /**
     * Sets up test data before each test.
     */
    @InjectMocks
    private AdminCompilationServiceImpl service;
    /**
     * Sets up test data before each test.
     */
    private CompilationRequest compilationRequest;
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
    @BeforeEach
    void setUp() {
        compilationRequest = CompilationRequest.builder()
                .title("Test Compilation")
                .pinned(true)
                .events(Collections.singletonList(1L))
                .build();

        compilationEntity = CompilationEntity.builder()
                .id(1)
                .title("Test Compilation")
                .pinned(true)
                .events(Collections.singletonList(new EventEntity()))
                .build();

        compilationResponse = CompilationResponse.builder()
                .id(1)
                .title("Test Compilation")
                .pinned(true)
                .events(Collections.emptyList())
                .build();
    }

    /**
     * Tests the createCompilation method.
     */
    @Test
    void testCreateCompilation() {
        when(mapper.toEntity(any(CompilationRequest.class),
                any(EventService.class)))
                .thenReturn(compilationEntity);
        when(repository.save(any(CompilationEntity.class)))
                .thenReturn(compilationEntity);
        when(mapper.toResponse(any(CompilationEntity.class)))
                .thenReturn(compilationResponse);

        CompilationResponse response = service
                .createCompilation(compilationRequest);

        verify(repository, times(1))
                .save(any(CompilationEntity.class));
        assert response != null;
        assert response.getId().equals(compilationEntity.getId());
    }

    /**
     * Tests the deleteCompilationById method.
     */
    @Test
    void testDeleteCompilationById() {
        when(repository.existsById(anyInt())).thenReturn(true);
        doNothing().when(repository).deleteById(anyInt());

        service.deleteCompilationById(1);

        verify(repository, times(1)).existsById(anyInt());
        verify(repository, times(1)).deleteById(anyInt());
    }

    /**
     * Tests the deleteCompilationById method for a non-existing compilation.
     */
    @Test
    void testDeleteCompilationByIdNotExist() {
        when(repository.existsById(anyInt())).thenReturn(false);

        try {
            service.deleteCompilationById(1);
        } catch (NotExistException e) {
            assert e.getMessage().equals("This compilation not exist");
        }

        verify(repository, times(1)).existsById(anyInt());
        verify(repository, times(0)).deleteById(anyInt());
    }

    /**
     * Tests the updateCompilation method.
     */
    @Test
    void testUpdateCompilation() {
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(compilationEntity));
        when(eventService.getEventEntities(anyList()))
                .thenReturn(Collections.singletonList(new EventEntity()));
        when(repository.save(any(CompilationEntity.class)))
                .thenReturn(compilationEntity);
        when(mapper.toResponse(any(CompilationEntity.class)))
                .thenReturn(compilationResponse);

        CompilationResponse response = service
                .updateCompilation(compilationRequest, 1);

        verify(repository, times(1))
                .findById(anyInt());
        verify(repository, times(1))
                .save(any(CompilationEntity.class));
        assert response != null;
        assert response.getId().equals(compilationEntity.getId());
    }

    /**
     * Tests the updateCompilation method for a non-existing compilation.
     */
    @Test
    void testUpdateCompilationNotExist() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            service.updateCompilation(compilationRequest, 1);
        } catch (NotExistException e) {
            assert e.getMessage().equals("This compilation does not exist");
        }

        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(0))
                .save(any(CompilationEntity.class));
    }
}
