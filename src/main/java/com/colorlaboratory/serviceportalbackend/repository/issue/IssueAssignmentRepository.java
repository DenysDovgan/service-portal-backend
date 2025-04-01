package com.colorlaboratory.serviceportalbackend.repository.issue;

import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssueAssignmentRepository extends JpaRepository<IssueAssignment, Long> {

    @Query("SELECT COUNT(a) > 0 FROM IssueAssignment a " +
            "WHERE a.issue.id = :issueId AND a.technician.id = :technicianId")
    boolean existsByIssueIdAndTechnicianId(@Param("issueId") Long issueId, @Param("technicianId") Long technicianId);

    void deleteByIssueId(Long issueId);

    long countByTechnicianId(Long userId);

    long countByTechnicianIdAndIssueStatus_Open(Long userId);

    long countByTechnicianIdAndIssueStatus_InProgress(Long userId);

    long countByTechnicianIdAndIssueStatus_Resolved(Long userId);

    long countByTechnicianIdAndIssueStatus_Closed(Long userId);
}
