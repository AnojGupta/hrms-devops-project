package com.example.hrms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    private String location;

    // The employee who manages this department (optional)
    @OneToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "department")
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();
}
