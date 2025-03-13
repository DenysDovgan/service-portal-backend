package com.colorlaboratory.serviceportalbackend.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Password can not be empty")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String oldPassword;

    @NotBlank(message = "Password can not be empty")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String newPassword;
}
