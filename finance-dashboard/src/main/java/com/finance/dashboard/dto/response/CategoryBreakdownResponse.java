package com.finance.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategoryBreakdownResponse {

    private String category;
    private String type;
    private BigDecimal total;
}
