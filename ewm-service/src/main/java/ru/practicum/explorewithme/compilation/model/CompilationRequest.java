package ru.practicum.explorewithme.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull
    @NotBlank
    @Size(max = MAX_TITLE_LENGTH)
    private String title;

    /**
     * Indicates if the compilation is pinned.
     */
//    @NotBlank
    private Boolean pinned;

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
