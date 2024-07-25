package ru.practicum.explorewithme.user.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.CategoryRequest;
import ru.practicum.explorewithme.category.model.CategoryResponse;
import ru.practicum.explorewithme.category.model.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.exists.ExistChecker;

/**
 * Implementation of AdminCategoryService that handles category operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository repository;
    private final ExistChecker checker;

    /**
     * Deletes a category by its ID.
     *
     * @param catId the ID of the category to delete
     */
    @Override
    @Transactional
    public void deleteCategory(final Integer catId) {
        log.info("Deleting category with id: {}", catId);
        checker.isCategoryExists(catId);
        repository.deleteById(catId);
        log.info("Deleted category with id: {}", catId);
    }

    /**
     * Creates a new category based on the provided category request.
     *
     * @param category the category request object containing details
     * @return the created category response
     */
    @Override
    @Transactional
    public CategoryResponse createCategory(final CategoryRequest category) {
        log.info("Creating category with name: {}", category.getName());
        CategoryEntity entity = repository.save(
                CategoryMapper.toEntity(category));
        CategoryResponse response = CategoryMapper.toResponse(entity);
        log.info("Created category with id: {}", response.getId());
        return response;
    }

    /**
     * Updates an existing category identified by its ID.
     *
     * @param category the category request object with updated details
     * @param catId    the ID of the category to update
     * @return the updated category response
     */
    @Override
    @Transactional
    public CategoryResponse updateCategory(final CategoryRequest category,
                                           final Integer catId) {
        log.info("Updating category with id: {}", catId);
        checker.isCategoryExists(catId);
        CategoryEntity categoryEntity = repository.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        if (category.getName() != null) {
            categoryEntity.setName(category.getName());
        }
        CategoryEntity saved = repository.save(categoryEntity);
        CategoryResponse response = CategoryMapper.toResponse(saved);
        log.info("Updated category with id: {}", response.getId());
        return response;
    }
}
