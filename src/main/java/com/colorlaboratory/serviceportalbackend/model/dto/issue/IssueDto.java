package com.colorlaboratory.serviceportalbackend.model.dto.issue;

import com.colorlaboratory.serviceportalbackend.model.dto.media.MediaDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class IssueDto {

    @NotNull
    private Long id;

    @NotNull
    private Long createdBy;

    @NotBlank
    @Size(min = 1, max = 255)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private IssueStatus status;

    @NotNull
    private LocalDateTime createdAt;
}
