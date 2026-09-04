package com.example.hrms.dto.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private Set<String> roles;
}
