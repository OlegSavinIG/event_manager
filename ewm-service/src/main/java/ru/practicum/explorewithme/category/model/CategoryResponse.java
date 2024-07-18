package ru.practicum.explorewithme.category.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for category responses.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    /**
     * The unique identifier of the category.
     */
    private Integer id;

    /**
     * The name of the category.
     */
    private String name;
}
