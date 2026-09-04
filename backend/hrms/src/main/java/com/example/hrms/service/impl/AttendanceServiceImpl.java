package com.example.hrms.service.impl;

import com.example.hrms.dto.request.CheckInRequest;
import com.example.hrms.dto.request.CheckOutRequest;
import com.example.hrms.dto.response.AttendanceResponse;
import com.example.hrms.entity.Attendance;
import com.example.hrms.entity.Employee;
import com.example.hrms.enums.AttendanceStatus;
import com.example.hrms.exception.BadRequestException;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.AttendanceMapper;
import com.example.hrms.repository.AttendanceRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public AttendanceResponse checkIn(CheckInRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        LocalDate today = LocalDate.now();
        attendanceRepository.findByEmployeeIdAndDate(employee.getId(), today)
                .ifPresent(a -> { throw new BadRequestException("Employee has already checked in today"); });

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .date(today)
                .checkInTime(LocalTime.now())
                .status(AttendanceStatus.PRESENT)
                .build();

        return AttendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    @Override
    @Transactional
    public AttendanceResponse checkOut(CheckOutRequest request) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(request.getEmployeeId(), today)
                .orElseThrow(() -> new BadRequestException("No check-in record found for today. Please check in first."));

        attendance.setCheckOutTime(LocalTime.now());
        return AttendanceMapper.toResponse(attendanceRepository.save(attendance));
    }

    @Override
    public List<AttendanceResponse> getAll() {
        return attendanceRepository.findAll().stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }

    @Override
    public List<AttendanceResponse> getByEmployee(Long employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId).stream()
                .map(AttendanceMapper::toResponse)
                .toList();
    }
}
