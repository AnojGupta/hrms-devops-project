package com.example.hrms.mapper;

import com.example.hrms.dto.response.PayrollResponse;
import com.example.hrms.entity.Payroll;

public class PayrollMapper {

    public static PayrollResponse toResponse(Payroll payroll) {
        if (payroll == null) return null;
        return PayrollResponse.builder()
                .id(payroll.getId())
                .employee(EmployeeMapper.toSummary(payroll.getEmployee()))
                .basicSalary(payroll.getBasicSalary())
                .bonus(payroll.getBonus())
                .deductions(payroll.getDeductions())
                .netSalary(payroll.getNetSalary())
                .paymentDate(payroll.getPaymentDate())
                .paymentStatus(payroll.getPaymentStatus())
                .build();
    }
}
