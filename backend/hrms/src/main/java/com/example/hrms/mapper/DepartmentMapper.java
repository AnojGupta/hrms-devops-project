package com.example.hrms.mapper;

import com.example.hrms.dto.response.DepartmentResponse;
import com.example.hrms.entity.Department;

public class DepartmentMapper {

    public static DepartmentResponse toResponse(Department department) {
        if (department == null) return null;
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .location(department.getLocation())
                .manager(EmployeeMapper.toSummary(department.getManager()))
                .employeeCount(department.getEmployees() != null ? department.getEmployees().size() : 0)
                .build();
    }
}
