package ru.practicum.explorewithme.category.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO for category requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    /**
     * Константа для максимальной длины имени категории.
     */
    private static final int MAX_NAME_LENGTH = 50;
    /**
     * The name of the category.
     */
    @NotNull
    @NotBlank
    @Size(max = MAX_NAME_LENGTH)
    private String name;
}
