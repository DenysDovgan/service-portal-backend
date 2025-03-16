package com.colorlaboratory.serviceportalbackend.service.media;

import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import com.colorlaboratory.serviceportalbackend.model.entity.media.Media;
import com.colorlaboratory.serviceportalbackend.repository.issue.IssueRepository;
import com.colorlaboratory.serviceportalbackend.repository.media.MediaRepository;
import com.colorlaboratory.serviceportalbackend.service.google_cloud_storage.GoogleCloudStorageService;
import com.colorlaboratory.serviceportalbackend.validator.media.MediaValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;
    private final MediaValidator mediaValidator;

    private final IssueRepository issueRepository;
    private final GoogleCloudStorageService cloudStorageService;

    @Transactional
    public void upload(Long issueId, MultipartFile[] files) {
        log.info("Uploading medias for issue with id {}", issueId);
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        mediaValidator.validateUpload(issue);

        for (MultipartFile file : files) {
            String fileUrl = cloudStorageService.uploadFile(file);

            Media media = Media.builder()
                    .issue(issue)
                    .url(fileUrl)
                    .type(file.getContentType())
                    .size(file.getSize())
                    .uploadedAt(LocalDateTime.now())
                    .build();

            mediaRepository.save(media);
            log.info("Media with url {} uploaded for issue with id {}", fileUrl, issueId);
        }
    }
}
