package com.colorlaboratory.serviceportalbackend.model.dto.media;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MediaViewDto {
    @NotNull
    private Long id;

    @NotNull
    private Long issueId;

    @NotBlank
    private String type;

    @NotNull
    private Long size;

    @NotNull
    private LocalDateTime uploadedAt;

    // This is the only URL exposed to the frontend
    @NotBlank
    private String signedUrl;
}
