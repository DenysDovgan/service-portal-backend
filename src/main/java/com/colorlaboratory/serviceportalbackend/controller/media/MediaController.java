package com.colorlaboratory.serviceportalbackend.controller.media;

import com.colorlaboratory.serviceportalbackend.model.dto.api.responses.ApiResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.media.DownloadMediaDto;
import com.colorlaboratory.serviceportalbackend.model.dto.media.responses.UploadMediaResponse;
import com.colorlaboratory.serviceportalbackend.service.media.MediaService;
import com.google.protobuf.Api;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Validated
public class MediaController {

    private final MediaService mediaService;

    public ResponseEntity<ApiResponse<Resource>> download(
            @PathVariable @NotNull @Positive Long mediaId
    ) {
        DownloadMediaDto media = mediaService.download(mediaId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + media.getFileName() + "\"")
                .body(ApiResponse.success(
                        "Media downloaded successfully",
                        new InputStreamResource(media.getInputStream())
                ));
    }

    @PostMapping("/{issueId}/upload")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<List<UploadMediaResponse>>> upload(
            @PathVariable Long issueId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Media uploaded successfully",
                mediaService.upload(issueId, files)
        ));
    }

    @DeleteMapping("/{mediaId}/delete")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<Object>> delete(
            @PathVariable @NotNull @Positive Long mediaId
    ) {
        mediaService.delete(mediaId);
        return ResponseEntity.ok(ApiResponse.success(
                "Media deleted successfully",
                null
        ));
    }
}
