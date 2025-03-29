package com.colorlaboratory.serviceportalbackend.controller.media;

import com.colorlaboratory.serviceportalbackend.model.dto.api.responses.ApiResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.media.responses.UploadMediaResponse;
import com.colorlaboratory.serviceportalbackend.service.media.MediaService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Validated
public class MediaController {

    private final MediaService mediaService;

    // TODO:: do multi files upload
    @PostMapping("/{issueId}/upload")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<UploadMediaResponse>> upload(
            @PathVariable Long issueId,
            @RequestParam("file") MultipartFile file
    ) {
        String url = mediaService.upload(issueId, file);
        return ResponseEntity.ok(ApiResponse.success(
                "Media uploaded successfully",
                new UploadMediaResponse(url)
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
