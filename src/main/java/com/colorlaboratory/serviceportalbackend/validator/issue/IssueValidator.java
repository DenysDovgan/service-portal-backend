package com.colorlaboratory.serviceportalbackend.validator.issue;

import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.CreateIssueRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IssueValidator {

    private final UserService userService;


    public void validatePublishIssue(Issue issue) {
        UserDto currentUser = userService.getCurrentUser();

        if (!issue.getCreatedBy().getId().equals(currentUser.getId())) {
            log.warn("User with id {} tried to publish issue with id {}", currentUser.getId(), issue.getId());
            throw new AccessDeniedException("You are not allowed to publish this issue");
        }

        if (issue.getIsPublished()) {
            throw new AccessDeniedException("Issue is already published");
        }
    }
}
