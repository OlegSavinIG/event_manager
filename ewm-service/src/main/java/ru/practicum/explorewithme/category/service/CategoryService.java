package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.CategoryResponse;

import java.util.List;

/**
 * Service interface for managing categories.
 */
public interface CategoryService {

    /**
     * Retrieves a list of categories with pagination.
     *
     * @param from the starting index of the result
     * @param size the number of results to retrieve
     * @return the list of category responses
     */
    List<CategoryResponse> getCategories(Integer from, Integer size);

    /**
     * Retrieves a specific category by its ID.
     *
     * @param catId the ID of the category
     * @return the category response
     */
    CategoryResponse getCategory(Integer catId);

    /**
     * Retrieves a specific category entity by its ID.
     *
     * @param catId the ID of the category
     * @return the category entity
     */
    CategoryEntity getCategoryEntity(Integer catId);
}
