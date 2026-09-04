package com.example.hrms.mapper;

import com.example.hrms.dto.response.AttendanceResponse;
import com.example.hrms.entity.Attendance;

public class AttendanceMapper {

    public static AttendanceResponse toResponse(Attendance attendance) {
        if (attendance == null) return null;
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .employee(EmployeeMapper.toSummary(attendance.getEmployee()))
                .date(attendance.getDate())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .status(attendance.getStatus())
                .build();
    }
}
