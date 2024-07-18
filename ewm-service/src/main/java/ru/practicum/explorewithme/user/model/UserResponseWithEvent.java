package ru.practicum.explorewithme.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object representing user information with limited details.
 * Typically used in contexts where only user ID and name are required.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseWithEvent {
    /**
     * Unique identifier of the user.
     */
    private Long id;

    /**
     * The name of the user.
     */
    private String name;
}
