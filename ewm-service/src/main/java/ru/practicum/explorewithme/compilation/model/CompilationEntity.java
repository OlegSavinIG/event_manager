package ru.practicum.explorewithme.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.model.EventEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Entity representing a compilation of events.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class CompilationEntity {

    /**
     * The unique identifier of the compilation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToMany
    @JoinTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<EventEntity> events;
}
