package ru.practicum.explorewithme.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.compilation.model.CompilationEntity;
import ru.practicum.explorewithme.compilation.model.CompilationMapper;
import ru.practicum.explorewithme.compilation.model.CompilationResponse;
import ru.practicum.explorewithme.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.exception.NotExistException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link CompilationService} interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    /**
     * REST repository for managing compilations.
     */
    private final CompilationRepository repository;
    /**
     * REST mapper for managing compilations.
     */
    private final CompilationMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CompilationResponse> getCompilations(final Boolean pinned,
                                                     final Integer from,
                                                     final Integer size) {
        log.info("Fetching compilations with pinned={}, from={}, size={}",
                pinned, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        List<CompilationEntity> entities = repository.findAllByPinned(pinned,
                pageable);
        List<CompilationResponse> responses = entities.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        log.info("Retrieved {} compilations", responses.size());
        return responses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CompilationResponse getCompilation(final Integer compId) {
        log.info("Fetching compilation with ID {}", compId);
        CompilationEntity entity = repository.findById(compId)
                .orElseThrow(() -> new NotExistException(
                        "Compilation doesn't exist"));
        CompilationResponse response = mapper.toResponse(entity);
        log.info("Compilation retrieved: {}", response);
        return response;
    }
}
