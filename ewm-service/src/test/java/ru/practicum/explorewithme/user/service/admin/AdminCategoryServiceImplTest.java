package ru.practicum.explorewithme.user.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.CategoryRequest;
import ru.practicum.explorewithme.category.model.CategoryResponse;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.exists.ExistChecker;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminCategoryServiceImplTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private ExistChecker checker;

    @InjectMocks
    private AdminCategoryServiceImpl service;

    private CategoryRequest categoryRequest;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setUp() {
        categoryRequest = CategoryRequest.builder()
                .name("TestCategory")
                .build();

        categoryEntity = CategoryEntity.builder()
                .id(1)
                .name("TestCategory")
                .build();
    }

    @Test
    void testCreateCategory() {
        when(repository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);

        CategoryResponse response = service.createCategory(categoryRequest);

        verify(repository, times(1)).save(any(CategoryEntity.class));
        assert response != null;
        assert response.getId().equals(categoryEntity.getId());
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(checker).isCategoryExists(anyInt());
        doNothing().when(repository).deleteById(anyInt());

        service.deleteCategory(1);

        verify(checker, times(1)).isCategoryExists(anyInt());
        verify(repository, times(1)).deleteById(anyInt());
    }

    @Test
    void testUpdateCategory() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(categoryEntity));
        when(repository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);

        CategoryResponse response = service.updateCategory(categoryRequest, 1);

        verify(checker, times(1)).isCategoryExists(anyInt());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(1)).save(any(CategoryEntity.class));
        assert response != null;
        assert response.getId().equals(categoryEntity.getId());
    }
}
