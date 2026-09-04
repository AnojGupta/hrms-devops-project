package com.example.hrms.dto.response;

import com.example.hrms.enums.EmploymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String jobTitle;
    private BigDecimal salary;
    private LocalDate joiningDate;
    private EmploymentStatus employmentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DepartmentSummaryResponse department;
    private EmployeeSummaryResponse manager;
    private AddressResponse address;
}
