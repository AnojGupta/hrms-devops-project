package com.example.hrms.service;

import com.example.hrms.dto.request.PayrollRequest;
import com.example.hrms.dto.response.PayrollResponse;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.Payroll;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.PayrollRepository;
import com.example.hrms.service.impl.PayrollServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayrollServiceImplTest {

    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private PayrollServiceImpl payrollService;

    @Test
    void create_shouldCalculateNetSalaryCorrectly() {
        Employee employee = Employee.builder().id(1L).firstName("Jane").lastName("Doe").build();

        PayrollRequest request = PayrollRequest.builder()
                .employeeId(1L)
                .basicSalary(new BigDecimal("50000"))
                .bonus(new BigDecimal("5000"))
                .deductions(new BigDecimal("2000"))
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(payrollRepository.save(any(Payroll.class))).thenAnswer(invocation -> {
            Payroll p = invocation.getArgument(0);
            p.setId(10L);
            return p;
        });

        PayrollResponse response = payrollService.create(request);

        // netSalary = basicSalary + bonus - deductions = 50000 + 5000 - 2000 = 53000
        assertThat(response.getNetSalary()).isEqualByComparingTo("53000");
    }
}
