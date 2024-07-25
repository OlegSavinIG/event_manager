package ru.practicum.explorewithme.user.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.model.UserRequest;
import ru.practicum.explorewithme.user.model.UserResponse;
import ru.practicum.explorewithme.user.service.admin.AdminUserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

/**
 * Controller for handling administrative operations on users.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserController {

    /**
     * Service for managing users.
     */
    private final AdminUserService service;

    /**
     * Adds a new user.
     *
     * @param userRequest the UserRequest object containing user data
     * @return ResponseEntity with the created UserResponse object
     */
    @PostMapping("/users")
    public ResponseEntity<UserResponse> addNewUser(
            @Valid @RequestBody final UserRequest userRequest) {
        return ResponseEntity.ok(service.addNewUser(userRequest));
    }

    /**
     * Retrieves user information based on optional IDs, paginated.
     *
     * @param ids  optional list of user IDs to filter results
     * @param from index of the first result to retrieve (default 0)
     * @param size maximum number of users to retrieve (default 10)
     * @return ResponseEntity with a collection of UserResponse objects
     */
    @GetMapping("/users")
    public ResponseEntity<Collection<UserResponse>> getUsersInformation(
            @RequestParam(required = false) final List<Long> ids,
            @PositiveOrZero @RequestParam(defaultValue = "0") final int from,
            @Positive @RequestParam(defaultValue = "10") final int size) {
        if (ids != null && !ids.isEmpty()) {
            return ResponseEntity.ok(service.findByIds(ids, from, size));
        } else {
            return ResponseEntity.ok(service.findAll(from, size));
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    @DeleteMapping("/users/{userId}")
    public void deleteUserById(@PathVariable final Long userId) {
        service.deleteUserById(userId);
    }
}
