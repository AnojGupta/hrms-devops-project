package com.example.hrms.dto.request;

import com.example.hrms.enums.EmploymentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {

    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private String phoneNumber;

    private String jobTitle;

    @DecimalMin(value = "0.0", inclusive = true, message = "Salary cannot be negative")
    private BigDecimal salary;

    private LocalDate joiningDate;

    private EmploymentStatus employmentStatus;

    private Long departmentId;

    private Long managerId;

    @Valid
    private AddressRequest address;
}
