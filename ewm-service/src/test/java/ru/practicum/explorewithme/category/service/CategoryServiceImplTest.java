package ru.practicum.explorewithme.category.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.CategoryResponse;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.exception.NotExistException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link CategoryServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryServiceImpl service;

    private CategoryEntity categoryEntity;
    private CategoryResponse categoryResponse;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    void setUp() {
        categoryEntity = CategoryEntity.builder()
                .id(1)
                .name("Test Category")
                .build();

        categoryResponse = CategoryResponse.builder()
                .id(1)
                .name("Test Category")
                .build();
    }

    /**
     * Tests the getCategories method.
     */
    @Test
    void getCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryEntity> categoryEntities = new PageImpl<>(
                Collections.singletonList(categoryEntity));

        when(repository.findAll(pageable)).thenReturn(categoryEntities);

        List<CategoryResponse> responses = service.getCategories(0, 10);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(categoryResponse, responses.get(0));

        verify(repository, times(1)).findAll(pageable);
    }

    /**
     * Tests the getCategory method for an existing category.
     */
    @Test
    void getCategory() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(categoryEntity));

        CategoryResponse response = service.getCategory(1);

        assertNotNull(response);
        assertEquals(categoryResponse, response);

        verify(repository, times(1)).findById(anyInt());
    }

    /**
     * Tests the getCategory method for a non-existing category.
     */
    @Test
    void getCategory_NotExist() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class,
                () -> service.getCategory(1));
        assertEquals("This category does not exist", exception.getMessage());

        verify(repository, times(1)).findById(anyInt());
    }

    /**
     * Tests the getCategoryEntity method for an existing category.
     */
    @Test
    void getCategoryEntity() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(categoryEntity));

        CategoryEntity response = service.getCategoryEntity(1);

        assertNotNull(response);
        assertEquals(categoryEntity, response);

        verify(repository, times(1)).findById(anyInt());
    }

    /**
     * Tests the getCategoryEntity method for a non-existing category.
     */
    @Test
    void getCategoryEntity_NotExist() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class,
                () -> service.getCategoryEntity(1));
        assertEquals("This category does not exist", exception.getMessage());

        verify(repository, times(1)).findById(anyInt());
    }
}
