package com.colorlaboratory.serviceportalbackend.model.dto.user;

import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 1, max = 20)
    private String phoneNumber;

    @NotBlank
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 1, max = 100)
    private String city;

    @NotBlank
    @Size(min = 1, max = 100)
    private String country;

    @Size(max = 255)
    private String companyName;

    @NotNull
    private Role role;
}
