package com.example.hrms.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private String location;
    private EmployeeSummaryResponse manager;
    private long employeeCount;
}
