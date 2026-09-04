package com.example.hrms.service;

import com.example.hrms.dto.request.LoginRequest;
import com.example.hrms.dto.request.RegisterRequest;
import com.example.hrms.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
