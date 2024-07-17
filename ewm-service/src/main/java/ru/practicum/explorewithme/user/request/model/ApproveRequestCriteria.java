package ru.practicum.explorewithme.user.request.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ApproveRequestCriteria {
    @NotNull
    @NotEmpty
    private List<Long> ids;
    @NotNull
    @NotBlank
    private String status;
}
