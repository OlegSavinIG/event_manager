package ru.practicum.explorewithme.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.user.model.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
@ToString(exclude = {"category", "initiator"})
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
    private String annotation;

    /**
     * The description of the event.
     */
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
    private boolean paid;

    /**
     * The number of views of the event.
     */
    private int views;

    /**
     * The number of confirmed requests for the event.
     */
    private int confirmedRequests;

    /**
     * The participant limit for the event.
     */
    private int participantLimit;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    /**
     * The initiator of the event.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity initiator;
}
