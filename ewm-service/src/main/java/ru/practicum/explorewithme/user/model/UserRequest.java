package ru.practicum.explorewithme.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Size(min = 2, max = 250)
    private String name;

    /**
     * The email address of the user.
     */
    @NotNull
    @NotBlank
    @Email
    @Size(min = 6, max = 64)
    private String email;
}
