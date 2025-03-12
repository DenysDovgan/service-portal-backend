package com.colorlaboratory.serviceportalbackend.model.entity.issue;

import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "issue_assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
