package com.colorlaboratory.serviceportalbackend.repository.issue;

import com.colorlaboratory.serviceportalbackend.model.dto.issue.responses.IssuePreviewResponse;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    Issue findIssueById(Long issueId);

    @Query(value = """
    SELECT DISTINCT i.* FROM issues i
    LEFT JOIN issues_assignment ia ON i.id = ia.issue_id
    WHERE (:status IS NULL OR i.status = :status)
      AND (:createdBy IS NULL OR i.created_by = :createdBy)
      AND (:assignedTo IS NULL OR ia.technician_id = :assignedTo)
    ORDER BY i.created_at DESC
    """, nativeQuery = true)
    List<Issue> filterByCriteria(
            @Param("status") String status,
            @Param("assignedTo") Long assignedTo,
            @Param("createdBy") Long createdBy
    );

    @Query(value = """
    SELECT new com.colorlaboratory.serviceportalbackend.model.dto.issue.responses.IssuePreviewResponse(
    i.id, i.title, i.status, cb.companyName, CONCAT(t.firstName, ' ', t.lastName), i.createdAt)
    FROM Issue i
    JOIN i.createdBy cb
    LEFT JOIN i.assignments a
    LEFT JOIN a.technician t
    WHERE i.deleted = false
    AND (:role = 'CLIENT' AND i.createdBy.id = :userId)
    OR (:role IN ('ADMIN', 'SERVICE_MANAGER') AND i.status != 'DRAFT')
    OR (:role = 'TECHNICIAN' AND a.technician.id = :userId AND i.status != 'DRAFT')
   """)
    List<IssuePreviewResponse> findPreviewsWithJoins(@Param("userId") Long userId, @Param("role") String role);

    List<Issue> findAllByDeletedFalseAndStatusNot(IssueStatus issueStatus);
    
    long countByCreatedByIdAndStatusAndDeletedFalse(Long userId, IssueStatus issueStatus);

    long countByCreatedByIdAndDeletedFalse(Long userId);

    long countByStatusAndDeletedFalse(IssueStatus issueStatus);

    long countByDeletedFalse();

    List<Issue> findAllByDeletedFalseAndCreatedBy_Id(Long createdById);
}
