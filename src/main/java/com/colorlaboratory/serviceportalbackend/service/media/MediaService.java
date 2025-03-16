package com.colorlaboratory.serviceportalbackend.service.media;

import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import com.colorlaboratory.serviceportalbackend.model.entity.media.Media;
import com.colorlaboratory.serviceportalbackend.repository.issue.IssueRepository;
import com.colorlaboratory.serviceportalbackend.repository.media.MediaRepository;
import com.colorlaboratory.serviceportalbackend.service.gcs.GcsService;
import com.colorlaboratory.serviceportalbackend.validator.media.MediaValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;
    private final MediaValidator mediaValidator;

    private final IssueRepository issueRepository;
    private final GcsService cloudStorageService;

    @Transactional
    public String upload(Long issueId, MultipartFile file) {
        log.info("Uploading medias for issue with id {}", issueId);
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        mediaValidator.validateUpload(issue);

        String fileUrl;

        try {
            fileUrl = cloudStorageService.uploadFile(file);
        } catch (IOException e) {
            log.error("Error occurred while uploading media for issue with id {}", issueId);
            throw new RuntimeException("Error occurred while uploading media");
        }

        Media media = Media.builder()
                .issue(issue)
                .url(fileUrl)
                .type(file.getContentType())
                .size(file.getSize())
                .uploadedAt(LocalDateTime.now())
                .build();

        mediaRepository.save(media);
        log.info("Media with url {} uploaded for issue with id {}", fileUrl, issueId);
        return fileUrl;
    }

    @Transactional
    public void delete(Long mediaId) {
        log.info("Deleting media with id {}", mediaId);
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new EntityNotFoundException("Media not found"));

        mediaValidator.validateDelete(media.getIssue());

        cloudStorageService.deleteFile(media.getUrl());
        mediaRepository.delete(media);
        log.info("Media with id {} deleted successfully", mediaId);
    }
}
