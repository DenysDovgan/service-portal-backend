package com.colorlaboratory.serviceportalbackend.model.dto.issue.responses;

import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IssuePreviewResponse {
    private Long id;
    private String title;
    private IssueStatus status;
    private String companyNameCreatedBy;
    private String nameAssignedTo;
    private LocalDateTime createdAt;

}
