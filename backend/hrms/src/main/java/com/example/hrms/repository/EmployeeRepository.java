package com.example.hrms.repository;

import com.example.hrms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> search(@Param("keyword") String keyword);

    List<Employee> findByManagerId(Long managerId);

    List<Employee> findByDepartmentId(Long departmentId);
}
