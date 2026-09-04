package com.example.hrms.controller;

import com.example.hrms.dto.request.LeaveDecisionRequest;
import com.example.hrms.dto.request.LeaveRequest;
import com.example.hrms.dto.response.LeaveResponse;
import com.example.hrms.service.LeaveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Tag(name = "Leave")
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    public ResponseEntity<LeaveResponse> create(@Valid @RequestBody LeaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<LeaveResponse>> getAll() {
        return ResponseEntity.ok(leaveService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getById(id));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveResponse> approve(@PathVariable Long id, @Valid @RequestBody LeaveDecisionRequest request) {
        return ResponseEntity.ok(leaveService.approve(id, request));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveResponse> reject(@PathVariable Long id, @Valid @RequestBody LeaveDecisionRequest request) {
        return ResponseEntity.ok(leaveService.reject(id, request));
    }
}
