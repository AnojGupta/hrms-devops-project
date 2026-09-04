package com.example.hrms.service;

import com.example.hrms.dto.request.PayrollRequest;
import com.example.hrms.dto.response.PayrollResponse;

import java.util.List;

public interface PayrollService {
    PayrollResponse create(PayrollRequest request);
    List<PayrollResponse> getAll();
    List<PayrollResponse> getByEmployee(Long employeeId);
}
