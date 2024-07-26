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
    private List<Long> events = new ArrayList<>();
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Boolean getPinned() { return pinned; }
    public void setPinned(Boolean pinned) { this.pinned = pinned; }

    public List<Long> getEvents() { return events; }
    public void setEvents(List<Long> events) { this.events = events; }
}
