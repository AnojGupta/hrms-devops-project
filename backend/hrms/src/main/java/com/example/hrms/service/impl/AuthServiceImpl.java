package com.example.hrms.service.impl;

import com.example.hrms.dto.request.LoginRequest;
import com.example.hrms.dto.request.RegisterRequest;
import com.example.hrms.dto.response.AuthResponse;
import com.example.hrms.entity.Role;
import com.example.hrms.entity.User;
import com.example.hrms.exception.BadRequestException;
import com.example.hrms.exception.DuplicateResourceException;
import com.example.hrms.repository.RoleRepository;
import com.example.hrms.repository.UserRepository;
import com.example.hrms.security.JwtUtil;
import com.example.hrms.security.UserPrincipal;
import com.example.hrms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username is already taken: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered: " + request.getEmail());
        }

        Set<String> requestedRoles = (request.getRoles() == null || request.getRoles().isEmpty())
                ? Set.of("EMPLOYEE")
                : request.getRoles();

        Set<Role> roles = new HashSet<>();
        for (String roleName : requestedRoles) {
            Role role = roleRepository.findByName(roleName.toUpperCase())
                    .orElseThrow(() -> new BadRequestException("Unknown role: " + roleName));
            roles.add(role);
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(roles)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(new UserPrincipal(user)))
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles.stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtil.generateToken(principal);

        Set<String> roleNames = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.replace("ROLE_", ""))
                .collect(Collectors.toSet());

        return AuthResponse.builder()
                .token(token)
                .username(principal.getUsername())
                .email(principal.getUser().getEmail())
                .roles(roleNames)
                .build();
    }
}
