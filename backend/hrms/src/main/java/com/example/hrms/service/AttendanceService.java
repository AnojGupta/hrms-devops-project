package com.example.hrms.service;

import com.example.hrms.dto.request.CheckInRequest;
import com.example.hrms.dto.request.CheckOutRequest;
import com.example.hrms.dto.response.AttendanceResponse;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse checkIn(CheckInRequest request);
    AttendanceResponse checkOut(CheckOutRequest request);
    List<AttendanceResponse> getAll();
    List<AttendanceResponse> getByEmployee(Long employeeId);
}
