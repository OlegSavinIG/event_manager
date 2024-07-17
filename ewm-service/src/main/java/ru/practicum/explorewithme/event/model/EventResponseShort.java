package ru.practicum.explorewithme.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.user.model.UserResponseWithEvent;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseShort {
    private String annotation;
    private LocalDateTime eventDate;
    private CategoryEntity category;
    private Integer confirmedRequests;
    private UserResponseWithEvent initiator;
    private Boolean paid;
    private Integer views;
    private String title;
    private Long id;
}
