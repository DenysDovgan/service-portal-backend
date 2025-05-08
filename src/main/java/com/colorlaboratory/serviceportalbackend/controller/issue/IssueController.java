package com.colorlaboratory.serviceportalbackend.controller.issue;

import com.colorlaboratory.serviceportalbackend.model.dto.issue.IssueDto;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.AssignTechnicianRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.CreateIssueRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.IssueStatusChangeRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.responses.IssuePreviewResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.responses.IssueResponse;
import com.colorlaboratory.serviceportalbackend.model.entity.issue.IssueStatus;
import com.colorlaboratory.serviceportalbackend.service.issue.IssueService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
@Validated
public class IssueController {

    private final IssueService issueService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<IssueDto>> getAll() {
        return ResponseEntity.ok(
                issueService.getAll()
        );
    }

    @GetMapping("/preview")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<IssuePreviewResponse>> getAllPreview() {
        return ResponseEntity.ok(
                issueService.getAllPreview()
        );
    }

    @GetMapping("/{issueId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IssueResponse> get(
            @PathVariable @NotNull @Positive Long issueId
    ) {
        return ResponseEntity.ok(
                issueService.get(issueId)
        );
    }

    @GetMapping("/filter")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<IssueDto>> filter(
            @RequestParam(required = false) IssueStatus status,
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(required = false) Long createdBy
    ) {
        return ResponseEntity.ok(
                issueService.filter(status, assignedTo, createdBy)
        );
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<IssueDto> create(
            @RequestBody @NotNull @Valid CreateIssueRequest request
    ) {
        return ResponseEntity.ok(
                issueService.create(request)
        );
    }

    @PutMapping("/{issueId}/assign")
    public ResponseEntity<IssueDto> assign(
            @PathVariable @NotNull @Positive Long issueId,
            @RequestBody @NotNull @Valid AssignTechnicianRequest request
    ) {
        return ResponseEntity.ok(issueService.assign(issueId, request));
    }

    @PutMapping("/{issueId}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IssueDto> status(
            @PathVariable @NotNull @Positive Long issueId,
            @RequestBody @NotNull @Valid IssueStatusChangeRequest request
    ) {
        return ResponseEntity.ok(
                issueService.changeStatus(issueId, request)
        );
    }

    @DeleteMapping("/{issueId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(
            @PathVariable @NotNull @Positive Long issueId
    ) {
        issueService.delete(issueId);
        return ResponseEntity.noContent().build();
    }
}
