package com.colorlaboratory.serviceportalbackend.service.insights;

import com.colorlaboratory.serviceportalbackend.model.dto.insights.responses.InsightsResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.insights.InsightsMetricType;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import com.colorlaboratory.serviceportalbackend.repository.issue.IssueAssignmentRepository;
import com.colorlaboratory.serviceportalbackend.repository.issue.IssueRepository;
import com.colorlaboratory.serviceportalbackend.repository.user.UserRepository;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsightsService {

    private final UserService userService;
    private final IssueRepository issueRepository;
    private final IssueAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    private UserDto currentUser;

    public InsightsResponse getInsights() {
        currentUser = userService.getCurrentUserDto();
        Role role = currentUser.getRole();

        if (role == Role.CLIENT) {
            return InsightsResponse.builder()
                    .clientMyTotalIssuesCount(count(InsightsMetricType.CLIENT_TOTAL))
                    .clientMyOpenedIssuesCount(count(InsightsMetricType.CLIENT_OPEN))
                    .clientMyInProgressIssuesCount(count(InsightsMetricType.CLIENT_IN_PROGRESS))
                    .clientMyResolvedIssuesCount(count(InsightsMetricType.CLIENT_RESOLVED))

                    .build();
        } else if (role == Role.TECHNICIAN) {
            return InsightsResponse.builder()
                    .staffMyAssignedIssuesCount(count(InsightsMetricType.STAFF_ASSIGNED))
                    .staffMyOpenIssuesCount(count(InsightsMetricType.STAFF_OPEN))
                    .staffMyInProgressIssuesCount(count(InsightsMetricType.STAFF_IN_PROGRESS))
                    .staffMyResolvedIssuesCount(count(InsightsMetricType.STAFF_RESOLVED))
                    .staffMyClosedIssuesCount(count(InsightsMetricType.STAFF_CLOSED))

                    .build();
        } else if (role == Role.SERVICE_MANAGER) {
            return InsightsResponse.builder()
                    .totalIssuesCount(count(InsightsMetricType.TOTAL_ISSUES))
                    .openIssuesCount(count(InsightsMetricType.OPEN_ISSUES))
                    .inProgressIssuesCount(count(InsightsMetricType.IN_PROGRESS_ISSUES))
                    .resolvedIssuesCount(count(InsightsMetricType.RESOLVED_ISSUES))
                    .closedIssuesCount(count(InsightsMetricType.CLOSED_ISSUES))

                    .totalServiceManagersCount(count(InsightsMetricType.SERVICE_MANAGERS))
                    .totalTechniciansCount(count(InsightsMetricType.TECHNICIANS))
                    .totalClientsCount(count(InsightsMetricType.CLIENTS))

                    .staffMyAssignedIssuesCount(count(InsightsMetricType.STAFF_ASSIGNED))
                    .staffMyOpenIssuesCount(count(InsightsMetricType.STAFF_OPEN))
                    .staffMyInProgressIssuesCount(count(InsightsMetricType.STAFF_IN_PROGRESS))
                    .staffMyResolvedIssuesCount(count(InsightsMetricType.STAFF_RESOLVED))
                    .staffMyClosedIssuesCount(count(InsightsMetricType.STAFF_CLOSED))

                    .build();
        } else if (role == Role.ADMIN) {
            return InsightsResponse.builder()
                    .totalIssuesCount(count(InsightsMetricType.TOTAL_ISSUES))
                    .openIssuesCount(count(InsightsMetricType.OPEN_ISSUES))
                    .inProgressIssuesCount(count(InsightsMetricType.IN_PROGRESS_ISSUES))
                    .resolvedIssuesCount(count(InsightsMetricType.RESOLVED_ISSUES))
                    .closedIssuesCount(count(InsightsMetricType.CLOSED_ISSUES))

                    .totalUsersCount(count(InsightsMetricType.TOTAL_USERS))
                    .totalAdminsCount(count(InsightsMetricType.ADMINS))
                    .totalServiceManagersCount(count(InsightsMetricType.SERVICE_MANAGERS))
                    .totalTechniciansCount(count(InsightsMetricType.TECHNICIANS))
                    .totalClientsCount(count(InsightsMetricType.CLIENTS))

                    .build();
        } else {
            return new InsightsResponse();
        }
    }

    private long count(InsightsMetricType type) {
        Long userId = currentUser.getId();

        return switch (type) {

            // ─── Global issue stats ──────────────────────────────────────────────
            case TOTAL_ISSUES -> issueRepository.countByDeletedFalse();
            case OPEN_ISSUES -> issueRepository.countByStatusAndDeletedFalse(IssueStatus.OPEN);
            case IN_PROGRESS_ISSUES -> issueRepository.countByStatusAndDeletedFalse(IssueStatus.IN_PROGRESS);
            case RESOLVED_ISSUES -> issueRepository.countByStatusAndDeletedFalse(IssueStatus.RESOLVED);
            case CLOSED_ISSUES -> issueRepository.countByStatusAndDeletedFalse(IssueStatus.CLOSED);

            // ─── User role stats ────────────────────────────────────────────────
            case TOTAL_USERS -> userRepository.count();
            case ADMINS -> userRepository.countByRole(Role.ADMIN);
            case SERVICE_MANAGERS -> userRepository.countByRole(Role.SERVICE_MANAGER);
            case TECHNICIANS -> userRepository.countByRole(Role.TECHNICIAN);
            case CLIENTS -> userRepository.countByRole(Role.CLIENT);

            // ─── Staff-assigned issues ───────────────────────────────────────────
            case STAFF_ASSIGNED -> assignmentRepository.countByTechnicianId(userId);
            case STAFF_OPEN -> assignmentRepository.countByTechnicianIdAndIssueStatus_Open(userId);
            case STAFF_IN_PROGRESS -> assignmentRepository.countByTechnicianIdAndIssueStatus_InProgress(userId);
            case STAFF_RESOLVED -> assignmentRepository.countByTechnicianIdAndIssueStatus_Resolved(userId);
            case STAFF_CLOSED -> assignmentRepository.countByTechnicianIdAndIssueStatus_Closed(userId);

            // ─── Client's own issues ─────────────────────────────────────────────
            case CLIENT_TOTAL -> issueRepository.countByCreatedByIdAndDeletedFalse(userId);
            case CLIENT_OPEN -> issueRepository.countByCreatedByIdAndStatusAndDeletedFalse(userId, IssueStatus.OPEN);
            case CLIENT_IN_PROGRESS -> issueRepository.countByCreatedByIdAndStatusAndDeletedFalse(userId, IssueStatus.IN_PROGRESS);
            case CLIENT_RESOLVED -> issueRepository.countByCreatedByIdAndStatusAndDeletedFalse(userId, IssueStatus.RESOLVED);

            default -> 0L; // fallback
        };
    }
}
