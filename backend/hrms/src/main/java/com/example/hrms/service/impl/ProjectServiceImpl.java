package com.example.hrms.service.impl;

import com.example.hrms.dto.request.AssignEmployeeProjectRequest;
import com.example.hrms.dto.request.ProjectRequest;
import com.example.hrms.dto.response.EmployeeProjectResponse;
import com.example.hrms.dto.response.ProjectResponse;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.EmployeeProject;
import com.example.hrms.entity.Project;
import com.example.hrms.exception.DuplicateResourceException;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.EmployeeProjectMapper;
import com.example.hrms.mapper.ProjectMapper;
import com.example.hrms.repository.EmployeeProjectRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.ProjectRepository;
import com.example.hrms.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeProjectRepository employeeProjectRepository;

    @Override
    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        if (projectRepository.existsByProjectCode(request.getProjectCode())) {
            throw new DuplicateResourceException("Project code already exists: " + request.getProjectCode());
        }
        Project project = new Project();
        applyRequestToEntity(project, request);
        return ProjectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public ProjectResponse getById(Long id) {
        return ProjectMapper.toResponse(findProjectOrThrow(id));
    }

    @Override
    public List<ProjectResponse> getAll() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = findProjectOrThrow(id);
        if (!project.getProjectCode().equals(request.getProjectCode())
                && projectRepository.existsByProjectCode(request.getProjectCode())) {
            throw new DuplicateResourceException("Project code already exists: " + request.getProjectCode());
        }
        applyRequestToEntity(project, request);
        return ProjectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Project project = findProjectOrThrow(id);
        projectRepository.delete(project);
    }

    @Override
    @Transactional
    public EmployeeProjectResponse assignEmployee(Long projectId, Long employeeId, AssignEmployeeProjectRequest request) {
        Project project = findProjectOrThrow(projectId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        employeeProjectRepository.findByEmployeeIdAndProjectId(employeeId, projectId)
                .ifPresent(ep -> {
                    throw new DuplicateResourceException("Employee is already assigned to this project");
                });

        EmployeeProject employeeProject = EmployeeProject.builder()
                .employee(employee)
                .project(project)
                .assignedDate(request.getAssignedDate() != null ? request.getAssignedDate() : LocalDate.now())
                .roleInProject(request.getRoleInProject())
                .build();

        return EmployeeProjectMapper.toResponse(employeeProjectRepository.save(employeeProject));
    }

    private void applyRequestToEntity(Project project, ProjectRequest request) {
        project.setProjectCode(request.getProjectCode());
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        project.setBudget(request.getBudget());
    }

    private Project findProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }
}
