package com.example.hrms.config;

import com.example.hrms.entity.Role;
import com.example.hrms.entity.User;
import com.example.hrms.repository.RoleRepository;
import com.example.hrms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

/**
 * Seeds the four base roles and a default ADMIN user on first startup,
 * so you always have a way to log in and start creating data through the API.
 *
 * Default admin credentials (CHANGE IN A REAL DEPLOYMENT):
 *   username: admin
 *   password: Admin@123
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = ensureRole("ADMIN", "Full system access");
        ensureRole("HR", "Manages employees, departments, attendance, leave, payroll");
        ensureRole("MANAGER", "Manages team members, approves leave, creates reviews");
        ensureRole("EMPLOYEE", "Standard employee self-service access");

        User admin;

        Optional<User> existingAdmin = userRepository.findByUsername("admin");

        if (existingAdmin.isPresent()) {
            admin = existingAdmin.get();

            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setEnabled(true);

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);

        } else {
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            admin = User.builder()
                    .username("admin")
                    .email("admin@hrms.local")
                    .password(passwordEncoder.encode("Admin@123"))
                    .enabled(true)
                    .roles(roles)
                    .build();

            userRepository.save(admin);
        }
    }

    private Role ensureRole(String name, String description) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name(name).description(description).build()
                ));
    }
}
