package com.example.hrms.repository;

import com.example.hrms.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByEmployeeId(Long employeeId);
}
