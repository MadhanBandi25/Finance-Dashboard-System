package com.finance.dashboard.dto.request;

import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateRecordRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Type is required (INCOME or EXPENSE)")
    private TransactionType transactionType;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Date is required")
    private LocalDate transactionDate;

    private String notes;
}
