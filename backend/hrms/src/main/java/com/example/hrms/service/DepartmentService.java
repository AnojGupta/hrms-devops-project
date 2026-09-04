package com.example.hrms.service;

import com.example.hrms.dto.request.DepartmentRequest;
import com.example.hrms.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse create(DepartmentRequest request);
    DepartmentResponse getById(Long id);
    List<DepartmentResponse> getAll();
    DepartmentResponse update(Long id, DepartmentRequest request);
    void delete(Long id);
}
