package com.colorlaboratory.serviceportalbackend.validator.media;

import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import com.colorlaboratory.serviceportalbackend.repository.media.MediaRepository;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class MediaValidator {

    private final UserService userService;
    private final MediaRepository mediaRepository;

    public void validateUpload(Issue issue) {
        UserDto currentUser = userService.getCurrentUserDto();

        if (!Objects.equals(issue.getCreatedBy().getId(), currentUser.getId())) {
            log.warn("User with id {} tried to upload media for issue with id {}", currentUser.getId(), issue.getId());
            throw new AccessDeniedException("You are not allowed to upload media for this issue");
        }

        if (issue.getStatus() != IssueStatus.DRAFT) {
            log.warn("User with id {} tried to upload media for published issue with id {}", currentUser.getId(), issue.getId());
            throw new AccessDeniedException("Cannot upload media. Issue is already published.");
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
