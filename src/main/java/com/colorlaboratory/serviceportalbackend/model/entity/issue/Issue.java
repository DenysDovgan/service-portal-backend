package com.colorlaboratory.serviceportalbackend.model.entity.issue;

import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
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
    private User client;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    private List<IssueMedia> media;
}
