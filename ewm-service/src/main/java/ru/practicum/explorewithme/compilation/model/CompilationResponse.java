package ru.practicum.explorewithme.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.model.EventResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for compilation responses.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponse {

    /**
     * The unique identifier of the compilation.
     */
    private Integer id;

    /**
     * The title of the compilation.
     */
    private String title;

    /**
     * Indicates if the compilation is pinned.
     */
    private Boolean pinned;

    /**
     * The list of events in the compilation.
     */
    private final List<EventResponse> events = new ArrayList<>();

    /**
     * The list of events in the compilation.
     * @return events
     */
    public List<EventResponse> getEvents() {
        return events;
    }

}
