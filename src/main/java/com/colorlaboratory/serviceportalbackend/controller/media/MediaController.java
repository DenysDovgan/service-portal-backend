package com.colorlaboratory.serviceportalbackend.controller.media;

import com.colorlaboratory.serviceportalbackend.model.dto.api.ApiResponse;
import com.colorlaboratory.serviceportalbackend.service.media.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    // TODO:: check for single file upload
    @PostMapping("/{issueId}/upload")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Map<String, String>> upload(
            @PathVariable Long issueId,
            @RequestParam("file") MultipartFile file
    ) {
        String url = mediaService.upload(issueId, file);
        return ResponseEntity.ok(ApiResponse.customMessage("url", url));
    }

    @DeleteMapping("/{mediaId}/delete")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long mediaId) {
        mediaService.delete(mediaId);
        return ResponseEntity.ok(ApiResponse.message("Media deleted successfully"));
    }
}
