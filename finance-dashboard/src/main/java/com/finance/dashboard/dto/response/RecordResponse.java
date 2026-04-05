package com.finance.dashboard.dto.response;

import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RecordResponse {

    private Long id;

    private Long userId;
    private String userName;

    private BigDecimal amount;

    private TransactionType transactionType;

    private Category category;

    private LocalDate transactionDate;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
