package com.example.hrms.config;

import com.example.hrms.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173","http://localhost:5174", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        // Employees
                        .requestMatchers(HttpMethod.POST, "/api/employees/**").hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasAnyRole("ADMIN", "HR", "MANAGER", "EMPLOYEE")

                        // Departments
                        .requestMatchers(HttpMethod.POST, "/api/departments/**").hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.PUT, "/api/departments/**").hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.DELETE, "/api/departments/**").hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.GET, "/api/departments/**").authenticated()

                        // Projects
                        .requestMatchers(HttpMethod.POST, "/api/projects/**").hasAnyRole("ADMIN", "HR", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasAnyRole("ADMIN", "HR", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasAnyRole("ADMIN", "HR")
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").authenticated()

                        // Attendance
                        .requestMatchers("/api/attendance/**").authenticated()

                        // Leave
                        .requestMatchers(HttpMethod.PUT, "/api/leaves/*/approve").hasAnyRole("ADMIN", "HR", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/leaves/*/reject").hasAnyRole("ADMIN", "HR", "MANAGER")
                        .requestMatchers("/api/leaves/**").authenticated()

                        // Payroll
                        .requestMatchers("/api/payroll/**").hasAnyRole("ADMIN", "HR")

                        // Performance reviews
                        .requestMatchers(HttpMethod.POST, "/api/performance-reviews/**").hasAnyRole("ADMIN", "HR", "MANAGER")
                        .requestMatchers("/api/performance-reviews/**").authenticated()

                        // Dashboard
                        .requestMatchers("/api/dashboard/**").authenticated()

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
