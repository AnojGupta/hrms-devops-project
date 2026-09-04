package com.example.hrms.mapper;

import com.example.hrms.dto.response.LeaveResponse;
import com.example.hrms.entity.Leave;

public class LeaveMapper {

    public static LeaveResponse toResponse(Leave leave) {
        if (leave == null) return null;
        return LeaveResponse.builder()
                .id(leave.getId())
                .employee(EmployeeMapper.toSummary(leave.getEmployee()))
                .leaveType(leave.getLeaveType())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .approvedBy(EmployeeMapper.toSummary(leave.getApprovedBy()))
                .createdAt(leave.getCreatedAt())
                .build();
    }
}
