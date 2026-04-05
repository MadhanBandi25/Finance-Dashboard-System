package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.CreateRecordRequest;
import com.finance.dashboard.dto.request.UpdateRecordRequest;
import com.finance.dashboard.dto.response.RecordResponse;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public interface FinancialRecordService {

    RecordResponse createRecord(CreateRecordRequest request);

    RecordResponse getRecordById(Long id);

    List<RecordResponse> getAllRecords();

    List<RecordResponse> getRecords(Long userId, String type, String category, LocalDate startDate, LocalDate endDate);

    RecordResponse updateRecord(Long id, UpdateRecordRequest request);

    void deleteRecord(Long id);
}
