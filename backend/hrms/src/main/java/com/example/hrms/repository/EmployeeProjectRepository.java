package com.example.hrms.repository;

import com.example.hrms.entity.EmployeeProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Long> {
    List<EmployeeProject> findByProjectId(Long projectId);
    List<EmployeeProject> findByEmployeeId(Long employeeId);
    Optional<EmployeeProject> findByEmployeeIdAndProjectId(Long employeeId, Long projectId);
}
