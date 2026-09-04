package com.example.hrms.service.impl;

import com.example.hrms.dto.request.PerformanceReviewRequest;
import com.example.hrms.dto.response.PerformanceReviewResponse;
import com.example.hrms.entity.Employee;
import com.example.hrms.entity.PerformanceReview;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.mapper.PerformanceReviewMapper;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.PerformanceReviewRepository;
import com.example.hrms.service.PerformanceReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    private final PerformanceReviewRepository performanceReviewRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public PerformanceReviewResponse create(PerformanceReviewRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));
        Employee reviewer = employeeRepository.findById(request.getReviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found with id: " + request.getReviewerId()));

        PerformanceReview review = PerformanceReview.builder()
                .employee(employee)
                .reviewer(reviewer)
                .rating(request.getRating())
                .feedback(request.getFeedback())
                .reviewDate(request.getReviewDate() != null ? request.getReviewDate() : LocalDate.now())
                .build();

        return PerformanceReviewMapper.toResponse(performanceReviewRepository.save(review));
    }

    @Override
    public List<PerformanceReviewResponse> getAll() {
        return performanceReviewRepository.findAll().stream()
                .map(PerformanceReviewMapper::toResponse)
                .toList();
    }

    @Override
    public List<PerformanceReviewResponse> getByEmployee(Long employeeId) {
        return performanceReviewRepository.findByEmployeeId(employeeId).stream()
                .map(PerformanceReviewMapper::toResponse)
                .toList();
    }
}
