package com.example.hrms.service.impl;

import com.example.hrms.dto.request.DepartmentRequest;
import com.example.hrms.dto.response.DepartmentResponse;
import com.example.hrms.entity.Department;
import com.example.hrms.entity.Employee;
import com.example.hrms.exception.DuplicateResourceException;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.DepartmentMapper;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department already exists with name: " + request.getName());
        }
        Department department = new Department();
        applyRequestToEntity(department, request);
        return DepartmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponse getById(Long id) {
        return DepartmentMapper.toResponse(findDepartmentOrThrow(id));
    }

    @Override
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream()
                .map(DepartmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = findDepartmentOrThrow(id);
        if (!department.getName().equals(request.getName()) && departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department already exists with name: " + request.getName());
        }
        applyRequestToEntity(department, request);
        return DepartmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Department department = findDepartmentOrThrow(id);
        departmentRepository.delete(department);
    }

    private void applyRequestToEntity(Department department, DepartmentRequest request) {
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setLocation(request.getLocation());

        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
            department.setManager(manager);
        } else {
            department.setManager(null);
        }
    }

    private Department findDepartmentOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }
}
