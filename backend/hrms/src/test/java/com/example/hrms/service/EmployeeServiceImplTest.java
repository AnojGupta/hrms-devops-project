package com.example.hrms.service;

import com.example.hrms.dto.request.EmployeeRequest;
import com.example.hrms.dto.response.EmployeeResponse;
import com.example.hrms.entity.Employee;
import com.example.hrms.exception.DuplicateResourceException;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeRequest request;

    @BeforeEach
    void setUp() {
        request = EmployeeRequest.builder()
                .employeeCode("EMP-100")
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .build();
    }

    @Test
    void create_shouldSaveEmployee_whenCodeAndEmailAreUnique() {
        when(employeeRepository.existsByEmployeeCode("EMP-100")).thenReturn(false);
        when(employeeRepository.existsByEmail("jane.doe@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            e.setId(1L);
            return e;
        });

        EmployeeResponse response = employeeService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmployeeCode()).isEqualTo("EMP-100");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void create_shouldThrow_whenEmployeeCodeAlreadyExists() {
        when(employeeRepository.existsByEmployeeCode("EMP-100")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("EMP-100");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getById_shouldThrow_whenEmployeeNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
