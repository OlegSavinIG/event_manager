package ru.practicum.explorewithme.category.model.mapper;

import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.category.model.CategoryRequest;
import ru.practicum.explorewithme.category.model.CategoryResponse;

/**
 * Mapper for converting between CategoryEntity and DTOs.
 */
public class CategoryMapper {
    protected CategoryMapper() {
    }

    /**
     * Converts a CategoryEntity to a CategoryResponse.
     *
     * @param categoryEntity the category entity to convert
     * @return the category response
     */
    public static CategoryResponse toResponse(
            final CategoryEntity categoryEntity) {
        return CategoryResponse.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .build();
    }

    /**
     * Converts a CategoryResponse to a CategoryEntity.
     *
     * @param category the category response to convert
     * @return the category entity
     */
    public static CategoryEntity toEntity(final CategoryResponse category) {
        return CategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    /**
     * Converts a CategoryRequest to a CategoryEntity.
     *
     * @param category the category request to convert
     * @return the category entity
     */
    public static CategoryEntity toEntity(final CategoryRequest category) {
        return CategoryEntity.builder()
                .name(category.getName())
                .build();
    }
}
