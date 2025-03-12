package com.colorlaboratory.serviceportalbackend.model.entity.issue;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "issue_media")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Column(nullable = false)
    private String url;
}
