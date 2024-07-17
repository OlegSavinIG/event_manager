package ru.practicum.explorewithme.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.model.CategoryRequest;
import ru.practicum.explorewithme.category.model.CategoryResponse;
import ru.practicum.explorewithme.user.service.admin.AdminCategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService service;


    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest category
    ) {
        return ResponseEntity.ok(service.createCategory(category));
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Integer catId) {
        service.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Integer catId,
            @Valid @RequestBody CategoryRequest category) {
        return ResponseEntity.ok(service.updateCategory(category, catId));
    }
}
