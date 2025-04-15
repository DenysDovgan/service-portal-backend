package com.colorlaboratory.serviceportalbackend.model.dto.issue.responses;

import com.colorlaboratory.serviceportalbackend.model.dto.media.MediaDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class IssueResponse {
    @NotNull
    private Long id;

    @NotNull
    private Long createdBy;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private IssueStatus status;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    private List<MediaDto> media;
}
