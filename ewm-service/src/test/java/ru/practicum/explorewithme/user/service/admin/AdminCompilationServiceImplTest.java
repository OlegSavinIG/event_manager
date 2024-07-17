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

@ExtendWith(MockitoExtension.class)
public class AdminCompilationServiceImplTest {

    @Mock
    private AdminCompilationRepository repository;

    @Mock
    private CompilationMapper mapper;

    @Mock
    private EventService eventService;

    @InjectMocks
    private AdminCompilationServiceImpl service;

    private CompilationRequest compilationRequest;
    private CompilationEntity compilationEntity;
    private CompilationResponse compilationResponse;

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

    @Test
    void testCreateCompilation() {
        when(mapper.toEntity(any(CompilationRequest.class), any(EventService.class)))
                .thenReturn(compilationEntity);
        when(repository.save(any(CompilationEntity.class)))
                .thenReturn(compilationEntity);
        when(mapper.toResponse(any(CompilationEntity.class)))
                .thenReturn(compilationResponse);

        CompilationResponse response = service.createCompilation(compilationRequest);

        verify(repository, times(1)).save(any(CompilationEntity.class));
        assert response != null;
        assert response.getId().equals(compilationEntity.getId());
    }

    @Test
    void testDeleteCompilationById() {
        when(repository.existsById(anyInt())).thenReturn(true);
        doNothing().when(repository).deleteById(anyInt());

        service.deleteCompilationById(1);

        verify(repository, times(1)).existsById(anyInt());
        verify(repository, times(1)).deleteById(anyInt());
    }

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

    @Test
    void testUpdateCompilation() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(compilationEntity));
        when(eventService.getEventEntities(anyList())).thenReturn(Collections.singletonList(new EventEntity()));
        when(repository.save(any(CompilationEntity.class))).thenReturn(compilationEntity);
        when(mapper.toResponse(any(CompilationEntity.class))).thenReturn(compilationResponse);

        CompilationResponse response = service.updateCompilation(compilationRequest, 1);

        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(1)).save(any(CompilationEntity.class));
        assert response != null;
        assert response.getId().equals(compilationEntity.getId());
    }

    @Test
    void testUpdateCompilationNotExist() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            service.updateCompilation(compilationRequest, 1);
        } catch (NotExistException e) {
            assert e.getMessage().equals("This compilation does not exist");
        }

        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(0)).save(any(CompilationEntity.class));
    }
}
