package com.colorlaboratory.serviceportalbackend.model.entity.user;

import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 1, max = 255)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "phone_number", nullable = false)
    @Size(min = 1, max = 20)
    private String phoneNumber;

    @Column(name = "first_name", nullable = false)
    @Size(min = 1, max = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(min = 1, max = 50)
    private String lastName;

    @Column(nullable = false)
    @Size(min = 1, max = 100)
    private String city;

    @Column(nullable = false)
    @Size(min = 1, max = 100)
    private String country;

    @Column(name = "company_name")
    @Size(max = 255)
    private String companyName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Issue> issues;
}
