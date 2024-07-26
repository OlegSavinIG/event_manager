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
    private List<EventResponse> events = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Boolean getPinned() { return pinned; }
    public void setPinned(Boolean pinned) { this.pinned = pinned; }

    public List<EventResponse> getEvents() { return events; }
    public void setEvents(List<EventResponse> events) { this.events = events; }
}
