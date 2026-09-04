package com.example.hrms.mapper;

import com.example.hrms.dto.response.*;
import com.example.hrms.entity.Address;
import com.example.hrms.entity.Employee;

public class EmployeeMapper {

    public static EmployeeSummaryResponse toSummary(Employee employee) {
        if (employee == null) return null;
        return EmployeeSummaryResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .jobTitle(employee.getJobTitle())
                .build();
    }

    public static AddressResponse toAddressResponse(Address address) {
        if (address == null) return null;
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

    public static EmployeeResponse toResponse(Employee employee) {
        if (employee == null) return null;
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .jobTitle(employee.getJobTitle())
                .salary(employee.getSalary())
                .joiningDate(employee.getJoiningDate())
                .employmentStatus(employee.getEmploymentStatus())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .department(employee.getDepartment() != null
                        ? DepartmentSummaryResponse.builder()
                            .id(employee.getDepartment().getId())
                            .name(employee.getDepartment().getName())
                            .build()
                        : null)
                .manager(toSummary(employee.getManager()))
                .address(toAddressResponse(employee.getAddress()))
                .build();
    }
}
