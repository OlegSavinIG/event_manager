package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.CategoryResponse;
import ru.practicum.explorewithme.category.model.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.exception.NotExistException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link CategoryService} interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories(Integer from, Integer size) {
        log.info("Getting categories from index {} with size {}", from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        Page<CategoryEntity> categoryEntities = repository.findAll(pageable);
        List<CategoryResponse> responses = categoryEntities.stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Retrieved {} categories", responses.size());
        return responses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Integer catId) {
        log.info("Getting category with ID {}", catId);
        CategoryEntity categoryEntity = repository.findById(catId)
                .orElseThrow(() -> new NotExistException(
                        "This category does not exist"));
        CategoryResponse response = CategoryMapper.toResponse(categoryEntity);
        log.info("Category retrieved: {}", response);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryEntity getCategoryEntity(Integer catId) {
        log.info("Getting category entity with ID {}", catId);
        CategoryEntity categoryEntity = repository.findById(catId)
                .orElseThrow(() -> new NotExistException(
                        "This category does not exist"));
        log.info("Category entity retrieved: {}", categoryEntity);
        return categoryEntity;
    }
}
