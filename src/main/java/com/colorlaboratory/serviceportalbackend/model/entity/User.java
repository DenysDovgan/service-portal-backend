package com.colorlaboratory.serviceportalbackend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 1, max = 50)
    private String username;

    @Email
    @Length(min = 1, max = 100)
    private String email;

    @Length(min = 1, max = 50)
    private String name;

    @Length(min = 1, max = 50)
    private String surname;

    @Length(min = 1, max = 100)
    private String companyName;

    @Length(min = 1, max = 255)
    private String password;

    private Role role;
}
