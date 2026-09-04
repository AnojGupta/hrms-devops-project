package com.example.hrms.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeProjectResponse {
    private Long id;
    private EmployeeSummaryResponse employee;
    private Long projectId;
    private String projectName;
    private LocalDate assignedDate;
    private String roleInProject;
}
