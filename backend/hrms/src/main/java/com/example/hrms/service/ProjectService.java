package com.example.hrms.service;

import com.example.hrms.dto.request.AssignEmployeeProjectRequest;
import com.example.hrms.dto.request.ProjectRequest;
import com.example.hrms.dto.response.EmployeeProjectResponse;
import com.example.hrms.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse create(ProjectRequest request);
    ProjectResponse getById(Long id);
    List<ProjectResponse> getAll();
    ProjectResponse update(Long id, ProjectRequest request);
    void delete(Long id);
    EmployeeProjectResponse assignEmployee(Long projectId, Long employeeId, AssignEmployeeProjectRequest request);
}
