package com.finance.dashboard.controller;

import com.finance.dashboard.dto.request.CreateRecordRequest;
import com.finance.dashboard.dto.request.UpdateRecordRequest;
import com.finance.dashboard.dto.response.RecordResponse;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.service.FinancialRecordService;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")

public class FinancialRecordController {

    @Autowired
    private FinancialRecordService financialRecordService;

    @PostMapping
    public ResponseEntity<RecordResponse> createRecord(@Valid @RequestBody CreateRecordRequest request){
        RecordResponse response =financialRecordService.createRecord(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordResponse> getRecordById(@PathVariable Long id){
        return ResponseEntity.ok(financialRecordService.getRecordById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RecordResponse>> getAllRecords(){
        return ResponseEntity.ok(financialRecordService.getAllRecords());
    }

    @GetMapping
    public ResponseEntity<List<RecordResponse>> getRecords(
            @RequestParam(required = false) Long userId,@RequestParam(required = false) String type,
            @RequestParam(required = false) String category,@RequestParam(required = false) LocalDate startDate,@RequestParam(required = false) LocalDate endDate){
        return ResponseEntity.ok(financialRecordService.getRecords(userId,type,category,startDate,endDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordResponse> updateRecord(@PathVariable Long id,@Valid @RequestBody UpdateRecordRequest request){
        return ResponseEntity.ok(financialRecordService.updateRecord(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long id){
        financialRecordService.deleteRecord(id);
        return ResponseEntity.ok("Record deleted successfully");
    }
}
