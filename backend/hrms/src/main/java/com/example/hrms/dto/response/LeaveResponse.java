package com.example.hrms.dto.response;

import com.example.hrms.enums.LeaveStatus;
import com.example.hrms.enums.LeaveType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveResponse {
    private Long id;
    private EmployeeSummaryResponse employee;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
    private EmployeeSummaryResponse approvedBy;
    private LocalDateTime createdAt;
}
