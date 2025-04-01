package com.colorlaboratory.serviceportalbackend.controller.insights;

import com.colorlaboratory.serviceportalbackend.model.dto.api.responses.ApiResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.insights.responses.InsightsResponse;
import com.colorlaboratory.serviceportalbackend.service.insights.InsightsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insights")
@RequiredArgsConstructor
public class InsightsController {

    private final InsightsService insightsService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<InsightsResponse>> getInsights() {
        return ResponseEntity.ok(ApiResponse.success(
                "Insights retrieved successfully",
                insightsService.getInsights()
        ));
    }
}
