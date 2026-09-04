package com.example.hrms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInRequest {

    @NotNull(message = "Employee id is required")
    private Long employeeId;
}
