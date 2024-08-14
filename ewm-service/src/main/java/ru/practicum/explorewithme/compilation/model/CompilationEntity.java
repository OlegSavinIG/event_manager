package ru.practicum.explorewithme.compilation.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.model.EventEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
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
    @Column(columnDefinition = "boolean default false")
    private Boolean pinned = false;

    /**
     * The list of events in the compilation.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private final List<EventEntity> events = new ArrayList<>();

    /**
     * The list of events in the compilation.
     *  @return events
     */
    public List<EventEntity> getEvents() {
        return events;
    }

}
