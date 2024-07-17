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

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationResponse> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Fetching compilations with pinned={}, from={}, size={}", pinned, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        List<CompilationEntity> entities = repository.findAllByPinned(pinned, pageable);
        List<CompilationResponse> responses = entities.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        log.info("Retrieved {} compilations", responses.size());
        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationResponse getCompilation(Integer compId) {
        log.info("Fetching compilation with ID {}", compId);
        CompilationEntity entity = repository.findById(compId)
                .orElseThrow(() -> new NotExistException("Compilation doesn't exist"));
        CompilationResponse response = mapper.toResponse(entity);
        log.info("Compilation retrieved: {}", response);
        return response;
    }
}
