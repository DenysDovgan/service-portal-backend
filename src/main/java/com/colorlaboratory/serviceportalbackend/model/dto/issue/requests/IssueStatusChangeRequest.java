package com.colorlaboratory.serviceportalbackend.model.dto.issue.requests;

import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class IssueStatusChangeRequest {
    private IssueStatus status;
}
