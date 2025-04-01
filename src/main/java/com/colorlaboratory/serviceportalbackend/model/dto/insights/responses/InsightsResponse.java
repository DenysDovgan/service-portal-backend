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
    private long totalIssuesCount;
    private long openIssuesCount;
    private long inProgressIssuesCount;
    private long resolvedIssuesCount;
    private long closedIssuesCount;

    // users
    private long totalUsersCount;
    private long totalAdminsCount;
    private long totalServiceManagersCount;
    private long totalTechniciansCount;
    private long totalClientsCount;

    // insights for staff
    private long staffMyAssignedIssuesCount;
    private long staffMyOpenIssuesCount;
    private long staffMyInProgressIssuesCount;
    private long staffMyResolvedIssuesCount;
    private long staffMyClosedIssuesCount;

    // insights for clients
    private long clientMyTotalIssuesCount;
    private long clientMyOpenedIssuesCount;
    private long clientMyInProgressIssuesCount;
    private long clientMyResolvedIssuesCount;
}
