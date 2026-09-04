package com.example.hrms.mapper;

import com.example.hrms.dto.response.EmployeeProjectResponse;
import com.example.hrms.entity.EmployeeProject;

public class EmployeeProjectMapper {

    public static EmployeeProjectResponse toResponse(EmployeeProject ep) {
        if (ep == null) return null;
        return EmployeeProjectResponse.builder()
                .id(ep.getId())
                .employee(EmployeeMapper.toSummary(ep.getEmployee()))
                .projectId(ep.getProject() != null ? ep.getProject().getId() : null)
                .projectName(ep.getProject() != null ? ep.getProject().getName() : null)
                .assignedDate(ep.getAssignedDate())
                .roleInProject(ep.getRoleInProject())
                .build();
    }
}
