package com.example.hrms.service;

import com.example.hrms.dto.request.LeaveDecisionRequest;
import com.example.hrms.dto.request.LeaveRequest;
import com.example.hrms.dto.response.LeaveResponse;

import java.util.List;

public interface LeaveService {
    LeaveResponse create(LeaveRequest request);
    List<LeaveResponse> getAll();
    LeaveResponse getById(Long id);
    LeaveResponse approve(Long id, LeaveDecisionRequest request);
    LeaveResponse reject(Long id, LeaveDecisionRequest request);
}
