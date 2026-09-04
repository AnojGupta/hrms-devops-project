package com.example.hrms.controller;

import com.example.hrms.dto.request.PayrollRequest;
import com.example.hrms.dto.response.PayrollResponse;
import com.example.hrms.service.PayrollService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@Tag(name = "Payroll")
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping
    public ResponseEntity<PayrollResponse> create(@Valid @RequestBody PayrollRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(payrollService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PayrollResponse>> getAll() {
        return ResponseEntity.ok(payrollService.getAll());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PayrollResponse>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(payrollService.getByEmployee(employeeId));
    }
}
