package com.colorlaboratory.serviceportalbackend.controller.issue;

import com.colorlaboratory.serviceportalbackend.model.dto.api.ApiResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.IssueDto;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.CreateIssueRequest;
import com.colorlaboratory.serviceportalbackend.service.issue.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<IssueDto> create(@RequestBody @Valid CreateIssueRequest request) {
        return ResponseEntity.ok(issueService.create(request));
    }

    @PutMapping("/{issueId}/publish")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Map<String, String>> publish(@PathVariable Long issueId) {
        issueService.publish(issueId);
        return ResponseEntity.ok(ApiResponse.message("Issue published successfully"));
    }
}
