package com.colorlaboratory.serviceportalbackend.model.dto.media.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UploadMediaResponse {
    private String url;
    private String type;
    private Long size;
}
