package ru.practicum.explorewithme.user.request.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Criteria object for approving requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApproveRequestCriteria {

    /**
     * List of IDs of requests to approve.
     * It should not be null or empty.
     */
    @NotNull(message = "Request IDs cannot be null.")
    @NotEmpty(message = "Request IDs cannot be empty.")
    @Builder.Default
    private List<Long> requestIds = new ArrayList<>();

    /**
     * Status to set for the approved requests.
     * It should not be null.
     */
    @NotNull(message = "Status cannot be null.")
    private String status;
}
