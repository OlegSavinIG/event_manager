package ru.practicum.explorewithme.compilation.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.annotation.DefaultValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for compilation requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationRequest {
    /**
     * Константа для максимальной длины title категории.
     */
    private static final int MAX_TITLE_LENGTH = 50;
    /**
     * The title of the compilation.
     */
    @NotNull(groups = DefaultValidation.class)
    @NotBlank(groups = DefaultValidation.class)
    @Size(max = MAX_TITLE_LENGTH)
    private String title;

    /**
     * Indicates if the compilation is pinned.
     */
    @Builder.Default
    private Boolean pinned = false;

    /**
     * The list of event IDs in the compilation.
     */
    private final List<Integer> events = new ArrayList<>();

    /**
     * The list of events in the compilation.
     * @return events
     */
     public List<Long> getEvents() {
        return events.stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());
    }

}
