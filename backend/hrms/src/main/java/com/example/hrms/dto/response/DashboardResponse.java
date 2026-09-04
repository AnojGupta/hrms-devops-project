package com.example.hrms.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {
    private long totalEmployees;
    private long totalDepartments;
    private long totalProjects;
    private long employeesOnLeaveToday;
    private long presentToday;
}
