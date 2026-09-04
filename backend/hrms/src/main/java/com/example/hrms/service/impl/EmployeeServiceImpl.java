package com.example.hrms.service.impl;

import com.example.hrms.dto.request.AddressRequest;
import com.example.hrms.dto.request.EmployeeRequest;
import com.example.hrms.dto.response.EmployeeResponse;
import com.example.hrms.entity.Address;
import com.example.hrms.entity.Department;
import com.example.hrms.entity.Employee;
import com.example.hrms.exception.DuplicateResourceException;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.EmployeeMapper;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateResourceException("Employee code already exists: " + request.getEmployeeCode());
        }
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        Employee employee = new Employee();
        applyRequestToEntity(employee, request);

        Employee saved = employeeRepository.save(employee);
        return EmployeeMapper.toResponse(saved);
    }

    @Override
    public EmployeeResponse getById(Long id) {
        Employee employee = findEmployeeOrThrow(id);
        return EmployeeMapper.toResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getAll() {
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = findEmployeeOrThrow(id);

        if (!employee.getEmployeeCode().equals(request.getEmployeeCode())
                && employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateResourceException("Employee code already exists: " + request.getEmployeeCode());
        }
        if (!employee.getEmail().equals(request.getEmail())
                && employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        applyRequestToEntity(employee, request);
        Employee saved = employeeRepository.save(employee);
        return EmployeeMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee employee = findEmployeeOrThrow(id);
        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeResponse> search(String keyword) {
        return employeeRepository.search(keyword).stream()
                .map(EmployeeMapper::toResponse)
                .toList();
    }

    private void applyRequestToEntity(Employee employee, EmployeeRequest request) {
        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setJobTitle(request.getJobTitle());
        employee.setSalary(request.getSalary());
        employee.setJoiningDate(request.getJoiningDate());
        if (request.getEmploymentStatus() != null) {
            employee.setEmploymentStatus(request.getEmploymentStatus());
        }

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        if (request.getManagerId() != null) {
            Employee manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + request.getManagerId()));
            employee.setManager(manager);
        } else {
            employee.setManager(null);
        }

        AddressRequest addressRequest = request.getAddress();
        if (addressRequest != null) {
            Address address = employee.getAddress() != null ? employee.getAddress() : new Address();
            address.setStreet(addressRequest.getStreet());
            address.setCity(addressRequest.getCity());
            address.setState(addressRequest.getState());
            address.setCountry(addressRequest.getCountry());
            address.setPostalCode(addressRequest.getPostalCode());
            employee.setAddress(address);
        }
    }

    private Employee findEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
}
