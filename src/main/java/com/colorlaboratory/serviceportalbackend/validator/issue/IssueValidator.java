package com.colorlaboratory.serviceportalbackend.validator.issue;

import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.IssueStatusChangeRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import com.colorlaboratory.serviceportalbackend.repository.issue.IssueAssigmentRepository;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class IssueValidator {

    private final UserService userService;
    private final IssueAssigmentRepository issueAssigmentRepository;


    public void validateGetIssue(Issue issue, UserDto currentUser) {
        boolean isClient = currentUser.getRole().equals(Role.CLIENT);
        boolean isTechnician = currentUser.getRole().equals(Role.TECHNICIAN);
        boolean isManagerOrAdmin = currentUser.getRole().equals(Role.SERVICE_MANAGER) || currentUser.getRole().equals(Role.ADMIN);

        if (isClient && !issue.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only view your own issues");
        }

        if (isTechnician) {
            boolean isAssigned = issueAssigmentRepository.existsByIssueIdAndTechnicianId(issue.getId(), currentUser.getId());
            if (!isAssigned || issue.getStatus() == IssueStatus.DRAFT) {
                throw new AccessDeniedException("You can only view assigned published issues");
            }
        }

        if (isManagerOrAdmin && issue.getStatus() == IssueStatus.DRAFT) {
            throw new AccessDeniedException("You can only view published issues");
        }
    }

    public void validateChangeStatus(Issue issue, IssueStatusChangeRequest request) {
        UserDto currentUser = userService.getCurrentUserDto();
        IssueStatus currentStatus = issue.getStatus();
        IssueStatus newStatus = request.getStatus();

        // Ownership check
        if (!issue.getCreatedBy().getId().equals(currentUser.getId())) {
            log.warn("User {} attempted to change status of issue {} not owned by them", currentUser.getId(), issue.getId());
            throw new AccessDeniedException("You do not have permission to change the status of this issue.");
        }

        // Allow non-client to close
        if (newStatus == IssueStatus.CLOSED && currentUser.getRole() != Role.CLIENT) {
            return;
        }

        // Clients restrictions
        if (currentUser.getRole() == Role.CLIENT) {
            if (newStatus != IssueStatus.OPEN && newStatus != IssueStatus.DRAFT) {
                throw new AccessDeniedException("Clients can only change status to OPEN or DRAFT.");
            }
        }

        // Prevent published → draft
        if (currentStatus != IssueStatus.DRAFT && newStatus == IssueStatus.DRAFT) {
            throw new AccessDeniedException("Once published, an issue cannot be reverted to DRAFT.");
        }

        // Enforce logical transitions
        if (currentStatus == IssueStatus.OPEN && newStatus != IssueStatus.IN_PROGRESS) {
            throw new AccessDeniedException("OPEN issues can only transition to IN_PROGRESS.");
        }

        if (currentStatus == IssueStatus.IN_PROGRESS && newStatus != IssueStatus.RESOLVED) {
            throw new AccessDeniedException("IN_PROGRESS issues can only transition to RESOLVED.");
        }
    }

    public void validateAssign(User user) {
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.SERVICE_MANAGER) {
            throw new AccessDeniedException("Only Admin or Service Manager can assign technicians.");
        }
    }
    public void validateFilter(UserDto currentUser, IssueStatus status, Long assignedTo, Long createdBy) {

        if (currentUser.getRole() == Role.ADMIN) {
            return; // Full access
        }

        Long currentUserId = currentUser.getId();

        // No one can view foreign drafts
        if (status == IssueStatus.DRAFT && (createdBy == null || !Objects.equals(currentUserId, createdBy))) {
            throw new AccessDeniedException("You are not allowed to view draft issues created by others.");
        }

        if (currentUser.getRole() == Role.CLIENT) {

            // Clients can’t fetch by another creator
            if (createdBy != null && !Objects.equals(currentUserId, createdBy)) {
                throw new AccessDeniedException("Clients can only filter issues they created.");
            }

            // If filtering by assignedTo (e.g., created by and assigned to someone), make sure it’s their own issue
            if (assignedTo != null && !Objects.equals(currentUserId, createdBy)) {
                throw new AccessDeniedException("Clients can only fetch assigned issues they personally created.");
            }
        }

        if (currentUser.getRole() == Role.TECHNICIAN) {
            if (assignedTo != null && !Objects.equals(currentUserId, assignedTo)) {
                throw new AccessDeniedException("Technicians can only view issues assigned to them.");
            }
        }
    }

    public void validateDelete(Issue issue, UserDto user) {
        Role role = user.getRole();
        Long userId = user.getId();

        if (role == Role.ADMIN) return;

        boolean isOwner = issue.getCreatedBy().getId().equals(userId);

        if (!isOwner) {
            throw new AccessDeniedException("You can only delete issues you created.");
        }

        if (issue.getDeleted()) {
            throw new AccessDeniedException("You can not delete already deleted issue");
        }

        if (issue.getStatus() == IssueStatus.DRAFT || issue.getStatus() == IssueStatus.RESOLVED || issue.getStatus() == IssueStatus.CLOSED) {
            return;
        }

        throw new AccessDeniedException("You can only delete DRAFT, RESOLVED, or CLOSED issues.");
    }

}
