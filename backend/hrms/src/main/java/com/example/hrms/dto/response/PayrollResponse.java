package com.example.hrms.dto.response;

import com.example.hrms.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollResponse {
    private Long id;
    private EmployeeSummaryResponse employee;
    private BigDecimal basicSalary;
    private BigDecimal bonus;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private LocalDate paymentDate;
    private PaymentStatus paymentStatus;
}
