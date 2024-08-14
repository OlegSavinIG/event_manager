package ru.practicum.explorewithme.user.request.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
     */
    @NotNull
    @NotEmpty
    private List<Integer> requestIds;

    /**
     * Status to set for the approved requests.
     */
    @NotNull
    @NotBlank
    private String status;
}
