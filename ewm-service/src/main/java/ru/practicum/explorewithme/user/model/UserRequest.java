package ru.practicum.explorewithme.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
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
     * Константа для максимальной длины name категории.
     */
    private static final int MAX_NAME_LENGTH = 250;
    /**
     * Константа для минимальной длины name категории.
     */
    private static final int MIN_NAME_LENGTH = 2;
    /**
     * Константа для максимальной длины email категории.
     */
    private static final int MAX_EMAIL_LENGTH = 254;
    /**
     * Константа для минимальной длины email категории.
     */
    private static final int MIN_EMAIL_LENGTH = 6;
    /**
     * The name of the user.
     */
    @NotNull
    @NotBlank
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    private String name;

    /**
     * The email address of the user.
     */
    @NotNull
    @NotBlank
    @Email
    @Size(min = MIN_EMAIL_LENGTH, max = MAX_EMAIL_LENGTH)
    private String email;
}
