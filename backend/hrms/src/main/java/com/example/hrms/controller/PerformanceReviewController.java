package com.example.hrms.controller;

import com.example.hrms.dto.request.PerformanceReviewRequest;
import com.example.hrms.dto.response.PerformanceReviewResponse;
import com.example.hrms.service.PerformanceReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performance-reviews")
@RequiredArgsConstructor
@Tag(name = "Performance Reviews")
public class PerformanceReviewController {

    private final PerformanceReviewService performanceReviewService;

    @PostMapping
    public ResponseEntity<PerformanceReviewResponse> create(@Valid @RequestBody PerformanceReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(performanceReviewService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PerformanceReviewResponse>> getAll() {
        return ResponseEntity.ok(performanceReviewService.getAll());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PerformanceReviewResponse>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(performanceReviewService.getByEmployee(employeeId));
    }
}
