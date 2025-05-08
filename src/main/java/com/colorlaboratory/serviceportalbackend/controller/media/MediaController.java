package com.colorlaboratory.serviceportalbackend.controller.media;

import com.colorlaboratory.serviceportalbackend.model.dto.media.DownloadMediaDto;
import com.colorlaboratory.serviceportalbackend.model.dto.media.responses.UploadMediaResponse;
import com.colorlaboratory.serviceportalbackend.service.media.MediaService;
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

    @GetMapping("/{mediaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> download(
            @PathVariable @NotNull @Positive Long mediaId
    ) {
        DownloadMediaDto media = mediaService.download(mediaId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + media.getFileName() + "\"")
                .body(
                        new InputStreamResource(media.getInputStream())
                );
    }

    @PostMapping("/{issueId}/upload")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<UploadMediaResponse>> upload(
            @PathVariable Long issueId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(
                mediaService.upload(issueId, files)
        );
    }

    @DeleteMapping("/{mediaId}/delete")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Void> delete(
            @PathVariable @NotNull @Positive Long mediaId
    ) {
        mediaService.delete(mediaId);
        return ResponseEntity.noContent().build();
    }
}
