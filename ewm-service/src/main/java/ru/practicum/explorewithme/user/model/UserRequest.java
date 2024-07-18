package ru.practicum.explorewithme.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Request object for creating or updating a user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    /**
     * The name of the user.
     */
    @NotNull
    @NotBlank
    private String name;

    /**
     * The email address of the user.
     */
    @NotNull
    @Email
    private String email;
}
