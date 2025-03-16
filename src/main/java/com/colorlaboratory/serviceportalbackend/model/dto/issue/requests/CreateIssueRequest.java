package com.colorlaboratory.serviceportalbackend.model.dto.issue.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateIssueRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
