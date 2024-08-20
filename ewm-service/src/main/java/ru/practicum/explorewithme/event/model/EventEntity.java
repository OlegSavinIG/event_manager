package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.user.model.UserEntity;
import ru.practicum.explorewithme.user.request.model.UserEventRequestEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
@ToString(exclude = {"category", "initiator", "confirmedRequests"})
public class EventEntity {

    /**
     * The unique identifier of the event.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title of the event.
     */
    private String title;

    /**
     * The annotation of the event.
     */
    @Column(columnDefinition = "TEXT")
    private String annotation;

    /**
     * The description of the event.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The creation date and time of the event.
     */
    @JsonProperty("createdOn")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * The date and time when the event will take place.
     */
    @JsonProperty("eventDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * The publication date and time of the event.
     */
    @JsonProperty("publishedOn")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    /**
     * Indicates if the event is paid.
     */
    @Column(columnDefinition = "boolean default false")
    private Boolean paid;

    /**
     * The number of views of the event.
     */
    private long views;

    /**
     * The confirmed requests for the event.
     */
    @OneToMany(mappedBy = "event")
    @Builder.Default
    private List<UserEventRequestEntity> confirmedRequests = new ArrayList<>();

    /**
     * The participant limit for the event.
     */
    private Integer participantLimit;

    /**
     * Indicates if the event requires request moderation.
     */
    private Boolean requestModeration;

    /**
     * The status of the event.
     */
    @Enumerated(EnumType.STRING)
    private EventStatus state;

    /**
     * The category of the event.
     */
    @ManyToOne()
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    /**
     * The initiator of the event.
     */
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity initiator;
}
