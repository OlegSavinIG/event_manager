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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link CategoryServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    /**
     * Sets up test data before each test.
     */
    @Mock
    private CategoryRepository repository;
    /**
     * Sets up test data before each test.
     */
    @InjectMocks
    private CategoryServiceImpl service;
    /**
     * Sets up test data before each test.
     */
    private CategoryEntity categoryEntity;
    /**
     * Sets up test data before each test.
     */
    private CategoryResponse categoryResponse;
    /**
     * Sets up test data before each test.
     */
   private int pageSize = 10;
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
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<CategoryEntity> categoryEntities = new PageImpl<>(
                Collections.singletonList(categoryEntity));

        when(repository.findAll(pageable)).thenReturn(categoryEntities);

        List<CategoryResponse> responses = service.getCategories(0, pageSize);

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
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(categoryEntity));

        CategoryResponse response = service.getCategory(1);

        assertNotNull(response);
        assertEquals(categoryResponse, response);

        verify(repository, times(1)).findById(anyInt());
    }

    /**
     * Tests the getCategory method for a non-existing category.
     */
    @Test
    void getCategoryNotExist() {
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
        when(repository.findById(anyInt()))
                .thenReturn(Optional.of(categoryEntity));

        CategoryEntity response = service.getCategoryEntity(1);

        assertNotNull(response);
        assertEquals(categoryEntity, response);

        verify(repository, times(1)).findById(anyInt());
    }

    /**
     * Tests the getCategoryEntity method for a non-existing category.
     */
    @Test
    void getCategoryEntityNotExist() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class,
                () -> service.getCategoryEntity(1));
        assertEquals("This category does not exist", exception.getMessage());

        verify(repository, times(1)).findById(anyInt());
    }
}
