package com.finance.dashboard.service;

import com.finance.dashboard.dto.response.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface DashboardService {

    SummaryResponse getSummary(Long userId, LocalDate startDate, LocalDate endDate);
    List<CategoryBreakdownResponse> getCategoryBreakdown(Long userId, LocalDate startDate, LocalDate endDate);
    List<TrendResponse> getMonthlyTrends(Long userId, LocalDate startDate,LocalDate endDate);
    List<WeeklyTrendResponse> getWeeklyTrends(Long userId, LocalDate startDate, LocalDate endDate);
    List<RecordResponse> getRecentActivity();
}
