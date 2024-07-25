package ru.practicum.explorewithme.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.model.CategoryRequest;
import ru.practicum.explorewithme.category.model.CategoryResponse;
import ru.practicum.explorewithme.user.service.admin.AdminCategoryService;

import javax.validation.Valid;

/**
 * REST controller for managing categories by admin.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCategoryController {

    /**
     * Service for managing categories.
     */
    private final AdminCategoryService service;

    /**
     * Creates a new category.
     *
     * @param category the category request to create
     * @return the created category response
     */
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody final CategoryRequest category) {
        return ResponseEntity.ok(service.createCategory(category));
    }

    /**
     * Deletes a category by its ID.
     *
     * @param catId the ID of the category to delete
     */
    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable final Integer catId) {
        service.deleteCategory(catId);
    }

    /**
     * Updates a category by its ID.
     *
     * @param catId    the ID of the category to update
     * @param category the category request with updated information
     * @return the updated category response
     */
    @PatchMapping("/categories/{catId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable final Integer catId,
            @Valid @RequestBody final CategoryRequest category) {
        return ResponseEntity.ok(service.updateCategory(category, catId));
    }
}
