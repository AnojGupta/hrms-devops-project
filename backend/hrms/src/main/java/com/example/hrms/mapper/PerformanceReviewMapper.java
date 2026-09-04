package com.example.hrms.mapper;

import com.example.hrms.dto.response.PerformanceReviewResponse;
import com.example.hrms.entity.PerformanceReview;

public class PerformanceReviewMapper {

    public static PerformanceReviewResponse toResponse(PerformanceReview review) {
        if (review == null) return null;
        return PerformanceReviewResponse.builder()
                .id(review.getId())
                .employee(EmployeeMapper.toSummary(review.getEmployee()))
                .reviewer(EmployeeMapper.toSummary(review.getReviewer()))
                .rating(review.getRating())
                .feedback(review.getFeedback())
                .reviewDate(review.getReviewDate())
                .build();
    }
}
