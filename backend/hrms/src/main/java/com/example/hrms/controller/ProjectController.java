package com.example.hrms.controller;

import com.example.hrms.dto.request.AssignEmployeeProjectRequest;
import com.example.hrms.dto.request.ProjectRequest;
import com.example.hrms.dto.response.EmployeeProjectResponse;
import com.example.hrms.dto.response.ProjectResponse;
import com.example.hrms.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/employees/{employeeId}")
    public ResponseEntity<EmployeeProjectResponse> assignEmployee(
            @PathVariable Long projectId,
            @PathVariable Long employeeId,
            @RequestBody(required = false) AssignEmployeeProjectRequest request) {
        AssignEmployeeProjectRequest body = request != null ? request : new AssignEmployeeProjectRequest();
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.assignEmployee(projectId, employeeId, body));
    }
}
