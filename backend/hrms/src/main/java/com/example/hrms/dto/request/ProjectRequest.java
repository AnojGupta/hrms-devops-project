package com.example.hrms.dto.request;

import com.example.hrms.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequest {

    @NotBlank(message = "Project code is required")
    private String projectCode;

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private BigDecimal budget;
}
