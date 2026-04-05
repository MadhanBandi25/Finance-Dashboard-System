package com.finance.dashboard.repository;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    List<FinancialRecord> findByIsDeletedFalse();

    List<FinancialRecord> findByUserIdAndIsDeletedFalse(Long userId);

    List<FinancialRecord> findTop5ByIsDeletedFalseOrderByCreatedAtDesc();

    @Query("""
        SELECT fr
        FROM FinancialRecord fr
        WHERE fr.isDeleted = false
          AND (:userId IS NULL OR fr.user.id = :userId)
          AND (:transactionType IS NULL OR fr.transactionType = :transactionType)
          AND (:category IS NULL OR fr.category = :category)
          AND (:startDate IS NULL OR fr.transactionDate >= :startDate)
          AND (:endDate IS NULL OR fr.transactionDate <= :endDate)
        ORDER BY fr.transactionDate DESC
        """)
    List<FinancialRecord> findWithFilters(Long userId,
                                          TransactionType transactionType,
                                          Category category,
                                          LocalDate startDate,
                                          LocalDate endDate);
}
