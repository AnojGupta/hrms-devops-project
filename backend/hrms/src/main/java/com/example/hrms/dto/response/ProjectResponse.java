package com.example.hrms.dto.response;

import com.example.hrms.enums.ProjectStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {
    private Long id;
    private String projectCode;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private BigDecimal budget;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
