package com.colorlaboratory.serviceportalbackend.validator.media;

import com.colorlaboratory.serviceportalbackend.config.MediaProperties;
import com.colorlaboratory.serviceportalbackend.exceptions.media.FileSizeLimitExceededException;
import com.colorlaboratory.serviceportalbackend.exceptions.media.UnsupportedMimeTypeException;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import com.colorlaboratory.serviceportalbackend.model.entity.media.Media;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class MediaValidator {

    private final UserService userService;

    private final MediaProperties mediaProperties;

    public void validateUpload(Issue issue, int newFilesCount) {
        UserDto currentUser = userService.getCurrentUserDto();

        if (!Objects.equals(issue.getCreatedBy().getId(), currentUser.getId())) {
            log.warn("User with id {} tried to upload media for issue with id {}", currentUser.getId(), issue.getId());
            throw new AccessDeniedException("You are not allowed to upload media for this issue");
        }

        if (issue.getStatus() != IssueStatus.DRAFT) {
            log.warn("User with id {} tried to upload media for published issue with id {}", currentUser.getId(), issue.getId());
            throw new AccessDeniedException("Cannot upload media. Issue is already published.");
        }

        int currentCount = issue.getMedia().size();
        if (currentCount + newFilesCount > 10) {
            throw new IllegalArgumentException("You can upload up to 10 files per issue. Current: " + currentCount + ", Attempting: " + newFilesCount);
        }
    }

    public void validateFile(MultipartFile file, Long issueId, Long createdById) {
        String contentType = file.getContentType();
        long size = file.getSize();

        if (contentType == null || !mediaProperties.getAllowedMimeTypes().contains(contentType)) {
            log.warn("User with id: {} tried to upload file with unsupported mime type: '{}' for issue with id: {}", createdById, contentType, issueId);
            throw new UnsupportedMimeTypeException("This file type is not supported");
        }

        boolean isImage = contentType.startsWith("image/");

        long maxAllowed = isImage
                ? mediaProperties.getMaxSize().getOrDefault("photo", 10_485_760L)
                : mediaProperties.getMaxSize().getOrDefault("video", 52_428_800L);

        if (size > maxAllowed) {
            log.warn("User with id: {} tried to upload file with size of: {}MB for issue with id: {}", createdById, size / 1024 / 1024, issueId);
            throw new FileSizeLimitExceededException("File is too large. Max allowed: " + (maxAllowed / 1024 / 1024) + " MB.");
        }
    }

    public void validateDelete(Issue issue) {
        UserDto currentUser = userService.getCurrentUserDto();

        if (!Objects.equals(issue.getCreatedBy().getId(), currentUser.getId())) {
            log.warn("User with id {} tried to delete media for issue with id {}", currentUser.getId(), issue.getId());
            throw new AccessDeniedException("You are not allowed to delete media for this issue");
        }

        if (issue.getStatus() != IssueStatus.DRAFT) {
            throw new AccessDeniedException("Cannot delete media. Issue is already published.");
        }
    }

}
