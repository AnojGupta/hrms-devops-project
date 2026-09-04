package com.example.hrms.dto.response;

import com.example.hrms.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {
    private Long id;
    private EmployeeSummaryResponse employee;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private AttendanceStatus status;
}
