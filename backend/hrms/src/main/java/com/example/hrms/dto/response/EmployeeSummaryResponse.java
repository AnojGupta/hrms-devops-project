package com.example.hrms.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSummaryResponse {
    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String jobTitle;
}
