package com.colorlaboratory.serviceportalbackend.validator.media;

import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
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

    public void validateUpload(Issue issue) {
        UserDto currentUser = userService.getCurrentUser();

        if (!Objects.equals(issue.getCreatedBy().getId(), currentUser.getId())) {
            log.error("User with id {} is not allowed to upload media for issue with id {}", currentUser.getId(), issue.getId());
            throw new AccessDeniedException("You are not allowed to upload media for this issue");
        }
    }
}
