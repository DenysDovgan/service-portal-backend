package com.colorlaboratory.serviceportalbackend.model.dto.media;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaPreviewDto {
    @NotNull
    private Long id;

    @NotBlank
    private String type;

    @NotNull
    private Long size;
}
