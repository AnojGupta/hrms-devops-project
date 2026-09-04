package com.example.hrms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveDecisionRequest {

    @NotNull(message = "Approver employee id is required")
    private Long approverEmployeeId;
}
