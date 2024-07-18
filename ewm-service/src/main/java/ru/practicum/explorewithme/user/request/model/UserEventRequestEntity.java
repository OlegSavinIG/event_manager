package ru.practicum.explorewithme.user.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.user.model.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class representing a user event request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
@ToString(exclude = {"event", "requester"})
public class UserEventRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Date and time when the request was created.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    /**
     * Event associated with the request.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    /**
     * User who made the request.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity requester;

    /**
     * Status of the request.
     */
    private String status;
}
