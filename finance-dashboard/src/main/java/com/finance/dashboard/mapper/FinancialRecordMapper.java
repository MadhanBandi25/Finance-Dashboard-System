package com.finance.dashboard.mapper;

import com.finance.dashboard.dto.request.CreateRecordRequest;
import com.finance.dashboard.dto.request.UpdateRecordRequest;
import com.finance.dashboard.dto.response.RecordResponse;
import com.finance.dashboard.entity.FinancialRecord;

public class FinancialRecordMapper {

    public static FinancialRecord toEntity(CreateRecordRequest request){

        if (request==null) return null;

        FinancialRecord record = new FinancialRecord();

        record.setAmount(request.getAmount());
        record.setTransactionType(request.getTransactionType());
        record.setCategory(request.getCategory());
        record.setTransactionDate(request.getTransactionDate());
        record.setNotes(request.getNotes());

        return record;
    }

    public static RecordResponse toResponse(FinancialRecord record){
        if (record == null) return null;

        RecordResponse response = new RecordResponse();

        response.setId(record.getId());
        if (record.getUser() != null){
            response.setUserId(record.getUser().getId());
            response.setUserName(record.getUser().getName());
        }

        response.setAmount(record.getAmount());
        response.setTransactionType(record.getTransactionType());
        response.setCategory(record.getCategory());
        response.setTransactionDate(record.getTransactionDate());
        response.setNotes(record.getNotes());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());

        return response;
    }

    public static void updateEntity(FinancialRecord record, UpdateRecordRequest request){

        if (record == null || request == null) return;

        if (request.getAmount() != null) {
            record.setAmount(request.getAmount());
        }

        if (request.getTransactionType() != null) {
            record.setTransactionType(request.getTransactionType());
        }

        if (request.getCategory() != null) {
            record.setCategory(request.getCategory());
        }

        if (request.getTransactionDate() != null) {
            record.setTransactionDate(request.getTransactionDate());
        }

        if (request.getNotes() != null) {
            record.setNotes(request.getNotes());
        }
    }
}
