package com.colorlaboratory.serviceportalbackend.model.entity.media;

import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "media")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;
}
