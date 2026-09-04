package com.example.hrms.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignEmployeeProjectRequest {
    private LocalDate assignedDate;
    private String roleInProject;
}
