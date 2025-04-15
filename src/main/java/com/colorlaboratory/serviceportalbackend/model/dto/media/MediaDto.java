package com.colorlaboratory.serviceportalbackend.model.dto.media;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MediaDto {

    @NotNull
    private Long id;

    @NotNull
    private Long issueId;

    @NotBlank
    private String url;

    @NotBlank
    @Size(min = 1, max = 50)
    private String type;

    @NotNull
    private Long size;

    @NotNull
    private LocalDateTime uploadedAt;
}
