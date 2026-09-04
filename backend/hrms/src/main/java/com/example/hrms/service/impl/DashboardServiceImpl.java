package com.example.hrms.service.impl;

import com.example.hrms.dto.response.DashboardResponse;
import com.example.hrms.enums.LeaveStatus;
import com.example.hrms.repository.*;
import com.example.hrms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final LeaveRepository leaveRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public DashboardResponse getSummary() {
        LocalDate today = LocalDate.now();

        long onLeaveToday = leaveRepository.findAll().stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .filter(l -> !today.isBefore(l.getStartDate()) && !today.isAfter(l.getEndDate()))
                .count();

        long presentToday = attendanceRepository.findAll().stream()
                .filter(a -> a.getDate().equals(today))
                .count();

        return DashboardResponse.builder()
                .totalEmployees(employeeRepository.count())
                .totalDepartments(departmentRepository.count())
                .totalProjects(projectRepository.count())
                .employeesOnLeaveToday(onLeaveToday)
                .presentToday(presentToday)
                .build();
    }
}
