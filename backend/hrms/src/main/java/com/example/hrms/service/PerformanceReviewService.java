package com.example.hrms.service;

import com.example.hrms.dto.request.PerformanceReviewRequest;
import com.example.hrms.dto.response.PerformanceReviewResponse;

import java.util.List;

public interface PerformanceReviewService {
    PerformanceReviewResponse create(PerformanceReviewRequest request);
    List<PerformanceReviewResponse> getAll();
    List<PerformanceReviewResponse> getByEmployee(Long employeeId);
}
