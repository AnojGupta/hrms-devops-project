package com.example.hrms.mapper;

import com.example.hrms.dto.response.ProjectResponse;
import com.example.hrms.entity.Project;

public class ProjectMapper {

    public static ProjectResponse toResponse(Project project) {
        if (project == null) return null;
        return ProjectResponse.builder()
                .id(project.getId())
                .projectCode(project.getProjectCode())
                .name(project.getName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .status(project.getStatus())
                .budget(project.getBudget())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
