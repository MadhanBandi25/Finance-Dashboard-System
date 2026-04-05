package com.finance.dashboard.service.IMPL;

import com.finance.dashboard.dto.response.*;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.enums.TransactionType;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.mapper.FinancialRecordMapper;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private FinancialRecordRepository financialRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public SummaryResponse getSummary(Long userId, LocalDate startDate, LocalDate endDate) {

        List<FinancialRecord> records = getFilteredRecords(userId,startDate,endDate);

        BigDecimal totalIncome = records.stream()
                .filter(record -> record.getTransactionType() == TransactionType.INCOME)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal totalExpenses = records.stream()
                .filter(record -> record.getTransactionType() == TransactionType.EXPENSE)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);
        return new SummaryResponse(totalIncome,totalExpenses,netBalance,startDate,endDate);
    }

    @Override
    public List<CategoryBreakdownResponse> getCategoryBreakdown(Long userId, LocalDate startDate, LocalDate endDate) {

        List<FinancialRecord> records = getFilteredRecords(userId, startDate, endDate);

        Map<String, BigDecimal> grouped = records.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getCategory().name() + "-" + record.getTransactionType().name(),
                        Collectors.reducing(BigDecimal.ZERO, FinancialRecord::getAmount, BigDecimal::add)
                ));

        List<CategoryBreakdownResponse> result = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : grouped.entrySet()) {
            String[] parts = entry.getKey().split("-");
            String category = parts[0];
            String type = parts[1];

            result.add(new CategoryBreakdownResponse(category, type, entry.getValue()));
        }

        result.sort(Comparator.comparing(CategoryBreakdownResponse::getCategory)
                .thenComparing(CategoryBreakdownResponse::getType));
        return result;
    }

    @Override
    public List<TrendResponse> getMonthlyTrends(Long userId, LocalDate startDate, LocalDate endDate) {
        List<FinancialRecord> records = getFilteredRecords(userId, startDate, endDate);

        Map<String, List<FinancialRecord>> grouped = records.stream()
                .collect(Collectors.groupingBy(record ->
                        record.getTransactionDate().getYear() + "-" + record.getTransactionDate().getMonthValue()
                ));

        List<TrendResponse> result = new ArrayList<>();

        for (List<FinancialRecord> monthRecords : grouped.values()) {
            FinancialRecord first = monthRecords.get(0);

            int year = first.getTransactionDate().getYear();
            int month = first.getTransactionDate().getMonthValue();
            String monthName = Month.of(month).name();

            BigDecimal totalIncome = monthRecords.stream()
                    .filter(record -> record.getTransactionType() == TransactionType.INCOME)
                    .map(FinancialRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalExpenses = monthRecords.stream()
                    .filter(record -> record.getTransactionType() == TransactionType.EXPENSE)
                    .map(FinancialRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal netBalance = totalIncome.subtract(totalExpenses);

            result.add(new TrendResponse(year, month, monthName, totalIncome, totalExpenses, netBalance));
        }

        result.sort(Comparator.comparing(TrendResponse::getYear)
                .thenComparing(TrendResponse::getMonth));
        return result;

    }

    @Override
    public List<WeeklyTrendResponse> getWeeklyTrends(Long userId, LocalDate startDate, LocalDate endDate) {
        List<FinancialRecord> records = getFilteredRecords(userId, startDate, endDate);

        WeekFields weekFields = WeekFields.ISO;

        Map<String, List<FinancialRecord>> grouped = records.stream()
                .collect(Collectors.groupingBy(record -> {
                    int weekYear = record.getTransactionDate().get(weekFields.weekBasedYear());
                    int week = record.getTransactionDate().get(weekFields.weekOfWeekBasedYear());
                    return weekYear + "-" + week;
                }));

        List<WeeklyTrendResponse> result = new ArrayList<>();

        for (List<FinancialRecord> weekRecords : grouped.values()) {
            FinancialRecord first = weekRecords.get(0);

            int year = first.getTransactionDate().get(weekFields.weekBasedYear());
            int week = first.getTransactionDate().get(weekFields.weekOfWeekBasedYear());

            BigDecimal totalIncome = weekRecords.stream()
                    .filter(record -> record.getTransactionType() == TransactionType.INCOME)
                    .map(FinancialRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalExpenses = weekRecords.stream()
                    .filter(record -> record.getTransactionType() == TransactionType.EXPENSE)
                    .map(FinancialRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal netBalance = totalIncome.subtract(totalExpenses);

            result.add(new WeeklyTrendResponse(year, week, totalIncome, totalExpenses, netBalance));
        }

        result.sort(Comparator.comparing(WeeklyTrendResponse::getYear)
                .thenComparing(WeeklyTrendResponse::getWeek));

        return result;
    }

    @Override
    public List<RecordResponse> getRecentActivity() {
        return financialRecordRepository.findTop5ByIsDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(FinancialRecordMapper::toResponse)
                .collect(Collectors.toList());
    }

    private List<FinancialRecord> getFilteredRecords(Long userId, LocalDate startDate, LocalDate endDate){

        if (userId != null && !userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        List<FinancialRecord> records;
        if (userId != null){
            records = financialRecordRepository.findByUserIdAndIsDeletedFalse(userId);
        } else {
            records = financialRecordRepository.findByIsDeletedFalse();
        }

        return records.stream()
                .filter(record -> startDate == null || !record.getTransactionDate().isBefore(startDate))
                .filter(record -> endDate == null || !record.getTransactionDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
}
