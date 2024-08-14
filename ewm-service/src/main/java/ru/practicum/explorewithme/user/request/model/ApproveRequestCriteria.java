package ru.practicum.explorewithme.user.request.model;

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
     */
    private List<Integer> requestIds = new ArrayList<>();

    /**
     * Status to set for the approved requests.
     */
    private String status;
}
