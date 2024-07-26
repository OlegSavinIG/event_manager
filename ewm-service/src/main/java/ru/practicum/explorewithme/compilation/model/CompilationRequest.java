package ru.practicum.explorewithme.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for compilation requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationRequest {

    /**
     * The title of the compilation.
     */
    @NotNull
    @NotBlank
    private String title;

    /**
     * Indicates if the compilation is pinned.
     */
    @NotNull
    private Boolean pinned;

    /**
     * The list of event IDs in the compilation.
     */
    private final List<Long> events = new ArrayList<>();

    /**
     * The list of events in the compilation.
     * @return events
     */
    public List<Long> getEvents() {
        return events;
    }

}
