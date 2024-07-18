package ru.practicum.explorewithme.user.request.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Criteria object for approving requests.
 */
@Getter
@Setter
public class ApproveRequestCriteria {

    /**
     * List of IDs of requests to approve.
     */
    @NotNull
    @NotEmpty
    private List<Long> ids;

    /**
     * Status to set for the approved requests.
     */
    @NotNull
    @NotBlank
    private String status;
}
