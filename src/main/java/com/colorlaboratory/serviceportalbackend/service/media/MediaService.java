package com.colorlaboratory.serviceportalbackend.service.media;

import com.colorlaboratory.serviceportalbackend.model.dto.media.responses.UploadMediaResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;
    private final MediaValidator mediaValidator;

    private final IssueRepository issueRepository;
    private final GcsService cloudStorageService;

    public String getSignedUrl(Media media) {
        log.info("Generating signed URL for media with id {}", media.getId());

        return cloudStorageService.generateSignedUrl(media.getUrl(), 15);
    }

    @Transactional
    public List<UploadMediaResponse> upload(Long issueId, List<MultipartFile> files) {
        log.info("Uploading medias for issue with id {}", issueId);
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        mediaValidator.validateUpload(issue, files.size());

        List<UploadMediaResponse> mediaResponseList = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                String url = cloudStorageService.uploadFile(file);

                mediaValidator.validateFile(file, issueId, issue.getCreatedBy().getId());

                Media media = Media.builder()
                        .issue(issue)
                        .url(url)
                        .type(file.getContentType())
                        .size(file.getSize())
                        .uploadedAt(LocalDateTime.now())
                        .build();

                media = mediaRepository.save(media);

                UploadMediaResponse uploadMediaResponse = UploadMediaResponse.builder()
                        .url(url)
                        .type(media.getType())
                        .size(media.getSize())
                        .build();

                mediaResponseList.add(uploadMediaResponse);

                log.info("Media with id {} uploaded for issue with id {}", media.getId(), issueId);
            }
        } catch (IOException e) {
            log.error("Error occurred while uploading media files for issue with id {}", issueId);
            throw new RuntimeException("Error occurred while uploading media");
        }

        return mediaResponseList;
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
