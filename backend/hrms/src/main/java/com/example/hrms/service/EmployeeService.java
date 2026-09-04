package com.example.hrms.service;

import com.example.hrms.dto.request.EmployeeRequest;
import com.example.hrms.dto.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request);
    EmployeeResponse getById(Long id);
    List<EmployeeResponse> getAll();
    EmployeeResponse update(Long id, EmployeeRequest request);
    void delete(Long id);
    List<EmployeeResponse> search(String keyword);
}
