package com.example.hrms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollRequest {

    @NotNull(message = "Employee id is required")
    private Long employeeId;

    @NotNull(message = "Basic salary is required")
    private BigDecimal basicSalary;

    private BigDecimal bonus;
    private BigDecimal deductions;
    private LocalDate paymentDate;
}
