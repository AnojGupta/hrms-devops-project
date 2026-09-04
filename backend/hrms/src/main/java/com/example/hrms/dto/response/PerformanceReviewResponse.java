package com.example.hrms.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReviewResponse {
    private Long id;
    private EmployeeSummaryResponse employee;
    private EmployeeSummaryResponse reviewer;
    private Integer rating;
    private String feedback;
    private LocalDate reviewDate;
}
