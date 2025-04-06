package com.colorlaboratory.serviceportalbackend.model.dto.user.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 1, max = 20)
    private String phoneNumber;

    @NotBlank
    @Size(min = 1, max = 100)
    private String country;

    @Size(max = 255)
    private String companyName;

}
