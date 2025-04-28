package com.colorlaboratory.serviceportalbackend.model.dto.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadMediaDto {
    private InputStream inputStream;
    private String contentType;
    private String fileName;
}
