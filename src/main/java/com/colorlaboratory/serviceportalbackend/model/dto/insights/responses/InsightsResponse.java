package com.colorlaboratory.serviceportalbackend.model.dto.insights.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsightsResponse {
    // issues
    private Long totalIssuesCount;
    private Long openIssuesCount;
    private Long inProgressIssuesCount;
    private Long resolvedIssuesCount;
    private Long closedIssuesCount;

    // users
    private Long totalUsersCount;
    private Long totalAdminsCount;
    private Long totalServiceManagersCount;
    private Long totalTechniciansCount;
    private Long totalClientsCount;

    // insights for staff
    private Long staffMyAssignedIssuesCount;
    private Long staffMyOpenIssuesCount;
    private Long staffMyInProgressIssuesCount;
    private Long staffMyResolvedIssuesCount;
    private Long staffMyClosedIssuesCount;

    // insights for clients
    private Long clientMyTotalIssuesCount;
    private Long clientMyOpenedIssuesCount;
    private Long clientMyInProgressIssuesCount;
    private Long clientMyResolvedIssuesCount;
}
