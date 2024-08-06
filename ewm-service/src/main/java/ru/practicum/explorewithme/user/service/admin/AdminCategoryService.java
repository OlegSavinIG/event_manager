package ru.practicum.explorewithme.user.service.admin;

import ru.practicum.explorewithme.category.model.CategoryRequest;
import ru.practicum.explorewithme.category.model.CategoryResponse;

/**
 * Service interface for managing categories in the admin context.
 */
public interface AdminCategoryService {

    /**
     * Deletes a category by its ID.
     *
     * @param catId the ID of the category to delete
     */
    void deleteCategory(Integer catId);

    /**
     * Creates a new category based on the provided category request.
     *
     * @param category the category request object containing details
     * @return the created category response
     */
    CategoryResponse createCategory(CategoryRequest category);

    /**
     * Updates an existing category identified by its ID.
     *
     * @param category the category request object with updated details
     * @param catId    the ID of the category to update
     * @return the updated category response
     */
    CategoryResponse updateCategory(CategoryRequest category,
                                    Integer catId);
}
