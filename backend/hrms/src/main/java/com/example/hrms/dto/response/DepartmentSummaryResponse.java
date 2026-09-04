package com.example.hrms.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentSummaryResponse {
    private Long id;
    private String name;
}
