package com.colorlaboratory.serviceportalbackend.model.entity.issue;

import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "issues_assignment")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class IssueAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "technician_id", nullable = false)
    private User technician;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    @Column(name = "assigned_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime assignedAt = LocalDateTime.now();
}
