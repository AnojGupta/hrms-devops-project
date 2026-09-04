package com.example.hrms.repository;

import com.example.hrms.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByProjectCode(String projectCode);
}
