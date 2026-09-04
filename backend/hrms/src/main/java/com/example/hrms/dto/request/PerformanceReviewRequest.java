package com.example.hrms.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReviewRequest {

    @NotNull(message = "Employee id is required")
    private Long employeeId;

    @NotNull(message = "Reviewer id is required")
    private Long reviewerId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    private String feedback;

    private LocalDate reviewDate;
}
