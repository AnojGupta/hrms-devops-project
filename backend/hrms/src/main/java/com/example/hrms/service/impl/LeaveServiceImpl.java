package com.example.hrms.service.impl;

import com.example.hrms.dto.request.LeaveDecisionRequest;
import com.example.hrms.dto.request.LeaveRequest;
import com.example.hrms.dto.response.LeaveResponse;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.Leave;
import com.example.hrms.enums.LeaveStatus;
import com.example.hrms.exception.BadRequestException;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.LeaveMapper;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.LeaveRepository;
import com.example.hrms.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public LeaveResponse create(LeaveRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        Leave leave = Leave.builder()
                .employee(employee)
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        return LeaveMapper.toResponse(leaveRepository.save(leave));
    }

    @Override
    public List<LeaveResponse> getAll() {
        return leaveRepository.findAll().stream()
                .map(LeaveMapper::toResponse)
                .toList();
    }

    @Override
    public LeaveResponse getById(Long id) {
        return LeaveMapper.toResponse(findLeaveOrThrow(id));
    }

    @Override
    @Transactional
    public LeaveResponse approve(Long id, LeaveDecisionRequest request) {
        Leave leave = findLeaveOrThrow(id);
        assertPending(leave);

        Employee approver = employeeRepository.findById(request.getApproverEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Approver employee not found with id: " + request.getApproverEmployeeId()));

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedBy(approver);
        return LeaveMapper.toResponse(leaveRepository.save(leave));
    }

    @Override
    @Transactional
    public LeaveResponse reject(Long id, LeaveDecisionRequest request) {
        Leave leave = findLeaveOrThrow(id);
        assertPending(leave);

        Employee approver = employeeRepository.findById(request.getApproverEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Approver employee not found with id: " + request.getApproverEmployeeId()));

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApprovedBy(approver);
        return LeaveMapper.toResponse(leaveRepository.save(leave));
    }

    private void assertPending(Leave leave) {
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Only PENDING leave requests can be approved or rejected");
        }
    }

    private Leave findLeaveOrThrow(Long id) {
        return leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));
    }
}
