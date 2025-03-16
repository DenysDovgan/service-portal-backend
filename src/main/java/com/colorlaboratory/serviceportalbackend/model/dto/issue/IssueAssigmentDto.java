package com.colorlaboratory.serviceportalbackend.model.dto.issue;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IssueAssigmentDto {

    @NotNull
    private Long id;

    @NotNull
    private Long issueId;

    @NotNull
    private Long technicianId;

    @NotNull
    private Long assignedBy;

    @NotNull
    private LocalDateTime assignedAt;
}
