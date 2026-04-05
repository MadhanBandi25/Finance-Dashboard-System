package com.finance.dashboard.service.IMPL;

import com.finance.dashboard.dto.request.CreateRecordRequest;
import com.finance.dashboard.dto.request.UpdateRecordRequest;
import com.finance.dashboard.dto.response.RecordResponse;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.TransactionType;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.mapper.FinancialRecordMapper;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.FinancialRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialRecordServiceImpl implements FinancialRecordService {

    @Autowired
    private FinancialRecordRepository financialRecordRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public RecordResponse createRecord(CreateRecordRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        FinancialRecord record = FinancialRecordMapper.toEntity(request);
        record.setUser(user);

        FinancialRecord savedRecord = financialRecordRepository.save(record);
        return FinancialRecordMapper.toResponse(savedRecord);
    }

    @Override
    public RecordResponse getRecordById(Long id) {

        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Record not found with id: "+ id));

        if (Boolean.TRUE.equals(record.getIsDeleted())){
            throw new ResourceNotFoundException("Record not found with id: "+ id);
        }
        return FinancialRecordMapper.toResponse(record);
    }

    @Override
    public List<RecordResponse> getAllRecords() {
        return financialRecordRepository.findByIsDeletedFalse()
                .stream()
                .map(FinancialRecordMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordResponse> getRecords(Long userId, String type, String category, LocalDate startDate, LocalDate endDate) {

        TransactionType transactionType = null;
        Category recordCategory = null;

        if (type != null && !type.isBlank()){
            try {
                transactionType = TransactionType.valueOf(type.toUpperCase());
            }catch (Exception exception){
                throw new IllegalArgumentException("Invalid transaction type: " + type);
            }
        }

        if (category != null && !category.isBlank()){
            try {
                recordCategory = Category.valueOf(category.toUpperCase());
            }catch (Exception ex){
                throw new IllegalArgumentException("Invalid category: "+ category);
            }
        }

        return financialRecordRepository.findWithFilters(userId,transactionType,recordCategory,startDate,endDate)
                .stream()
                .map(FinancialRecordMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RecordResponse updateRecord(Long id, UpdateRecordRequest request) {
        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        if (Boolean.TRUE.equals(record.getIsDeleted())) {
            throw new IllegalStateException("Cannot update deleted record");
        }

        FinancialRecordMapper.updateEntity(record, request);
        FinancialRecord updatedRecord = financialRecordRepository.save(record);

        return FinancialRecordMapper.toResponse(updatedRecord);
    }

    @Override
    public void deleteRecord(Long id) {
        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        if (Boolean.TRUE.equals(record.getIsDeleted())) {
            throw new IllegalStateException("Record already deleted");
        }

        record.setIsDeleted(true);
        financialRecordRepository.save(record);
    }
}
