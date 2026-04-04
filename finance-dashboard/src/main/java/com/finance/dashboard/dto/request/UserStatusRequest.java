package com.finance.dashboard.dto.request;

import com.finance.dashboard.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserStatusRequest {

    @NotNull(message = "Status is required (ACTIVE or INACTIVE)")
    private UserStatus status;
}
