package com.example.hrms.service.impl;

import com.example.hrms.dto.request.PayrollRequest;
import com.example.hrms.dto.response.PayrollResponse;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.Payroll;
import com.example.hrms.enums.PaymentStatus;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.PayrollMapper;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.PayrollRepository;
import com.example.hrms.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public PayrollResponse create(PayrollRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        BigDecimal bonus = request.getBonus() != null ? request.getBonus() : BigDecimal.ZERO;
        BigDecimal deductions = request.getDeductions() != null ? request.getDeductions() : BigDecimal.ZERO;

        // netSalary = basicSalary + bonus - deductions
        BigDecimal netSalary = request.getBasicSalary().add(bonus).subtract(deductions);

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .basicSalary(request.getBasicSalary())
                .bonus(bonus)
                .deductions(deductions)
                .netSalary(netSalary)
                .paymentDate(request.getPaymentDate())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        return PayrollMapper.toResponse(payrollRepository.save(payroll));
    }

    @Override
    public List<PayrollResponse> getAll() {
        return payrollRepository.findAll().stream()
                .map(PayrollMapper::toResponse)
                .toList();
    }

    @Override
    public List<PayrollResponse> getByEmployee(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId).stream()
                .map(PayrollMapper::toResponse)
                .toList();
    }
}
