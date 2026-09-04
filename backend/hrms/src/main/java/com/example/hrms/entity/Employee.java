package com.example.hrms.entity;

import com.example.hrms.enums.EmploymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String employeeCode;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    private String jobTitle;

    @Column(precision = 12, scale = 2)
    private BigDecimal salary;

    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EmploymentStatus employmentStatus = EmploymentStatus.ACTIVE;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    // Self-referencing many-to-one: this employee's manager
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    @Builder.Default
    private Set<Employee> directReports = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<EmployeeProject> projectAssignments = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Builder.Default
    private Set<Attendance> attendanceRecords = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Builder.Default
    private Set<Leave> leaveRecords = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Builder.Default
    private Set<Payroll> payrollRecords = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @Builder.Default
    private Set<PerformanceReview> performanceReviews = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
