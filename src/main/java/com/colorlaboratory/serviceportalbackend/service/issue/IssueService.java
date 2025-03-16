package com.colorlaboratory.serviceportalbackend.service.issue;

import com.colorlaboratory.serviceportalbackend.mapper.issue.IssueMapper;
import com.colorlaboratory.serviceportalbackend.mapper.user.UserMapper;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.IssueDto;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.CreateIssueRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.Issue;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import com.colorlaboratory.serviceportalbackend.repository.issue.IssueRepository;
import com.colorlaboratory.serviceportalbackend.service.media.MediaService;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import com.colorlaboratory.serviceportalbackend.validator.issue.IssueValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;
    private final IssueValidator issueValidator;

    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public IssueDto create(CreateIssueRequest request) {
        UserDto currentUser = userService.getCurrentUser();

        log.info("Client with id {} is creating an issue", currentUser.getId());

        Issue issue = Issue.builder()
                .createdBy(userMapper.toEntity(currentUser))
                .title(request.getTitle())
                .description(request.getDescription())
                .status(IssueStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return issueMapper.toDto(issueRepository.save(issue));
    }
}
