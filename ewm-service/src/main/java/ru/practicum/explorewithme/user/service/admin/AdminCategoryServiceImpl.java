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


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository repository;
    private final ExistChecker checker;

    @Override
    @Transactional
    public void deleteCategory(Integer catId) {
        log.info("Deleting category with id: {}", catId);
        checker.isCategoryExists(catId);
        repository.deleteById(catId);
        log.info("Deleted category with id: {}", catId);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest category) {
        log.info("Creating category with name: {}", category.getName());
        CategoryEntity entity = repository.save(CategoryMapper.toEntity(category));
        CategoryResponse response = CategoryMapper.toResponse(entity);
        log.info("Created category with id: {}", response.getId());
        return response;
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(CategoryRequest category, Integer catId) {
        log.info("Updating category with id: {}", catId);
        checker.isCategoryExists(catId);
        CategoryEntity categoryEntity = repository.findById(catId).get();
        if (category.getName() != null) {
            categoryEntity.setName(category.getName());
        }
        CategoryEntity saved = repository.save(categoryEntity);
        CategoryResponse response = CategoryMapper.toResponse(saved);
        log.info("Updated category with id: {}", response.getId());
        return response;
    }
}
