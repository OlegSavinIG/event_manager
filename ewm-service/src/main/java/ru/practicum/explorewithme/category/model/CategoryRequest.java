package ru.practicum.explorewithme.category.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO for category requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    /**
     * The name of the category.
     */
    @NotNull
    @NotBlank
    private String name;
}
