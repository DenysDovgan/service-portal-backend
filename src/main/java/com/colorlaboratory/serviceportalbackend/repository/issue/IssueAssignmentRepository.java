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

    @Query("""
    SELECT COUNT(ia)
    FROM IssueAssignment ia
    WHERE ia.technician.id = :userId
      AND ia.issue.status = 'OPEN'
      AND ia.issue.deleted = false
    """)
    long countByTechnicianIdAndIssueStatus_Open(@Param("userId") Long userId);

    @Query("""
    SELECT COUNT(ia)
    FROM IssueAssignment ia
    WHERE ia.technician.id = :userId
      AND ia.issue.status = 'IN_PROGRESS'
      AND ia.issue.deleted = false
    """)
    long countByTechnicianIdAndIssueStatus_InProgress(@Param("userId") Long userId);

    @Query("""
    SELECT COUNT(ia)
    FROM IssueAssignment ia
    WHERE ia.technician.id = :userId
      AND ia.issue.status = 'RESOLVED'
      AND ia.issue.deleted = false
    """)
    long countByTechnicianIdAndIssueStatus_Resolved(@Param("userId") Long userId);

    @Query("""
    SELECT COUNT(ia)
    FROM IssueAssignment ia
    WHERE ia.technician.id = :userId
      AND ia.issue.status = 'CLOSED'
      AND ia.issue.deleted = false
    """)
    long countByTechnicianIdAndIssueStatus_Closed(@Param("userId") Long userId);
}
