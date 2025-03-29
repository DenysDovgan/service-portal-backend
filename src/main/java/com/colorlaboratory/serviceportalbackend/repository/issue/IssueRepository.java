package com.colorlaboratory.serviceportalbackend.repository.issue;

import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
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
}
