package com.finance.dashboard.dto.request;

import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateRecordRequest {

    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    private TransactionType transactionType;
    private Category category;
    private LocalDate transactionDate;
    private String notes;
}
