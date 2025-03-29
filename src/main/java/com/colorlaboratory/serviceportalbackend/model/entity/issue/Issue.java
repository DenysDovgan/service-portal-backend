package com.colorlaboratory.serviceportalbackend.model.entity.issue;

import com.colorlaboratory.serviceportalbackend.model.entity.media.Media;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@SQLRestriction("deleted = false")
@Table(name = "issues")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IssueStatus status;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> media;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueAssignment> assignments;



    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
