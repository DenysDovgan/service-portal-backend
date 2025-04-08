package com.colorlaboratory.serviceportalbackend.controller.issue;

import com.colorlaboratory.serviceportalbackend.model.dto.api.responses.ApiResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.IssueDto;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.AssignTechnicianRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.CreateIssueRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.issue.requests.IssueStatusChangeRequest;
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
    public ResponseEntity<ApiResponse<List<IssueDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(
                "Issues retrieved successfully",
                issueService.getAll()
        ));
    }

    @GetMapping("/{issueId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<IssueDto>> get(
            @PathVariable @NotNull @Positive Long issueId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Issue retrieved successfully",
                issueService.get(issueId)
        ));
    }

    @GetMapping("/filter")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<IssueDto>>> filter(
            @RequestParam(required = false) IssueStatus status,
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(required = false) Long createdBy
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Issues filtered successfully",
                issueService.filter(status, assignedTo, createdBy)
        ));
    }



    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<IssueDto>> create(
            @RequestBody @NotNull @Valid CreateIssueRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Issue created successfully",
                issueService.create(request)
        ));
    }

    @PutMapping("/{issueId}/assign")
    public ResponseEntity<ApiResponse<Object>> assign(
            @PathVariable @NotNull @Positive Long issueId,
            @RequestBody @NotNull @Valid AssignTechnicianRequest request
    ) {
        issueService.assign(issueId, request);
        return ResponseEntity.ok(ApiResponse.success(
                "Issue assigned successfully",
                null
        ));
    }

    @PutMapping("/{issueId}/status")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<ApiResponse<IssueDto>> status(
            @PathVariable @NotNull @Positive Long issueId,
            @RequestBody @NotNull @Valid IssueStatusChangeRequest request
            ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Issue status changed successfully",
                issueService.status(issueId, request)
        ));
    }

    @DeleteMapping("/{issueId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Object>> delete(
            @PathVariable @NotNull @Positive Long issueId
    ) {
        issueService.delete(issueId);
        return ResponseEntity.ok(ApiResponse.success(
                "Issue has been deleted",
                null
        ));
    }
}
