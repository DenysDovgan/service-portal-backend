package com.colorlaboratory.serviceportalbackend.service.issue;

import com.colorlaboratory.serviceportalbackend.mapper.issue.IssueMapper;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.IssueDto;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.AssignTechnicianRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.CreateIssueRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.IssueStatusChangeRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueAssignment;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import com.colorlaboratory.serviceportalbackend.repository.issue.IssueAssignmentRepository;
import com.colorlaboratory.serviceportalbackend.repository.issue.IssueRepository;
import com.colorlaboratory.serviceportalbackend.repository.user.UserRepository;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import com.colorlaboratory.serviceportalbackend.validator.issue.IssueValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueAssignmentRepository issueAssignmentRepository;
    private final IssueMapper issueMapper;
    private final IssueValidator issueValidator;

    private final UserService userService;
    private final UserRepository userRepository;

    public List<IssueDto> getAll() {
        UserDto currentUser = userService.getCurrentUserDto();
        log.info("User (id: {}, role: {}) is trying to access all issues", currentUser.getId(), currentUser.getRole());

        List<Issue> issues;

        switch (currentUser.getRole()) {
            case ADMIN, SERVICE_MANAGER -> issues = issueRepository.findAllByDeletedFalseAndStatusNot(IssueStatus.DRAFT);
            case TECHNICIAN -> issues = issueAssignmentRepository.findAllIssuesByTechnicianIdAndIssueStatusIsNot_DraftAndDeleted_False(currentUser.getId());
            case CLIENT -> issues = issueRepository.findAllByDeletedFalseAndCreatedBy_Id(currentUser.getId());
            default -> throw new AccessDeniedException("Access denied");
        }

        return issueMapper.toDto(issues);
    }

    public IssueDto get(Long issueId) {
        UserDto currentUser = userService.getCurrentUserDto();
        log.info("User {} (role: {}) is trying to access issue {}", currentUser.getId(), currentUser.getRole(), issueId);

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        issueValidator.validateGetIssue(issue, currentUser);

        return issueMapper.toDto(issue);
    }

    @Transactional
    public IssueDto create(CreateIssueRequest request) {
        User currentUser = userService.getCurrentUser();

        Issue issue = Issue.builder()
                .createdBy(currentUser)
                .title(request.getTitle())
                .description(request.getDescription())
                .status(IssueStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        log.info("Creating issue for user with id {}", currentUser.getId());
        return issueMapper.toDto(issueRepository.save(issue));
    }

    // TODO: Clean implementation. Unnecessary assignments deletion
    public void assign(Long issueId, AssignTechnicianRequest request) {
        User currentUser = userService.getCurrentUser();

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        issueValidator.validateAssignRequest(currentUser.getRole());

        // Remove existing assignments
        issueAssignmentRepository.deleteByIssueId(issueId);

        // Create new assignments
        List<IssueAssignment> assignments = request.getTechnicianIds().stream()
                .map(techId -> {
                    User technician = userRepository.findById(techId)
                            .orElseThrow(() -> {
                                log.warn("Technician (id: {}) not found while trying to assign to issue (id: {})",
                                        techId, issue.getId());
                                return new EntityNotFoundException("Technician not found");
                            });
                    issueValidator.validateAssign(technician.getRole(), techId, currentUser.getRole(), currentUser.getId());

                    return IssueAssignment.builder()
                            .issue(issue)
                            .technician(technician)
                            .assignedBy(currentUser)
                            .assignedAt(LocalDateTime.now())
                            .build();
                })
                .toList();

        issueAssignmentRepository.saveAll(assignments);

        log.info("Issue {} assigned to technicians {} by {}", issueId, request.getTechnicianIds(), currentUser.getId());
    }

    public List<IssueDto> filter(IssueStatus status, Long assignedTo, Long createdBy) {
        UserDto currentUser = userService.getCurrentUserDto();

        issueValidator.validateFilter(currentUser, status, assignedTo, createdBy);

        String statusName = (status != null) ? status.name() : null;

        List<Issue> issues = issueRepository.filterByCriteria(
                statusName,
                assignedTo,
                createdBy
        );

        return issueMapper.toDto(issues);
    }


    @Transactional
    public IssueDto status(Long issueId, IssueStatusChangeRequest request) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        issueValidator.validateChangeStatus(issue, request);

        issue.setStatus(request.getStatus());
        issue.setUpdatedAt(LocalDateTime.now());

        IssueDto issueDto = issueMapper.toDto(issueRepository.save(issue));
        log.info("Issue with id {} published successfully", issueId);

        return issueDto;
    }

    @Transactional
    public void delete(Long issueId) {
        UserDto currentUser = userService.getCurrentUserDto();

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        issueValidator.validateDelete(issue, currentUser);

        switch (issue.getStatus()) {
            case DRAFT -> {
                issueRepository.delete(issue); // hard delete
                log.info("Issue {} hard-deleted by user {}", issueId, currentUser.getId());
            }
            case CLOSED, RESOLVED -> {
                issue.setDeleted(true);
                issue.setUpdatedAt(LocalDateTime.now());
                issueRepository.save(issue); // soft delete
                log.info("Issue {} soft-deleted by user {}", issueId, currentUser.getId());
            }
            default -> throw new AccessDeniedException("Only issues with status DRAFT, CLOSED, or RESOLVED can be deleted.");
        }
    }
}
