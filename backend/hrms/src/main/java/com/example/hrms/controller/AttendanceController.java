package com.example.hrms.controller;

import com.example.hrms.dto.request.CheckInRequest;
import com.example.hrms.dto.request.CheckOutRequest;
import com.example.hrms.dto.response.AttendanceResponse;
import com.example.hrms.service.AttendanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.checkIn(request));
    }

    @PutMapping("/check-out")
    public ResponseEntity<AttendanceResponse> checkOut(@Valid @RequestBody CheckOutRequest request) {
        return ResponseEntity.ok(attendanceService.checkOut(request));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAll() {
        return ResponseEntity.ok(attendanceService.getAll());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceResponse>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getByEmployee(employeeId));
    }
}
