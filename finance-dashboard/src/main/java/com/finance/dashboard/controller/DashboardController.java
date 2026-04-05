package com.finance.dashboard.controller;


import com.finance.dashboard.dto.response.*;
import com.finance.dashboard.service.DashboardService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary(@RequestParam(required = false) Long userId,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){

        return ResponseEntity.ok(dashboardService.getSummary(userId,startDate,endDate));
    }

    @GetMapping("/category-breakdown")
    public ResponseEntity<List<CategoryBreakdownResponse>> getCategoryBreakdown(@RequestParam(required = false) Long userId,
                                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return ResponseEntity.ok(dashboardService.getCategoryBreakdown(userId, startDate, endDate));
    }

    @GetMapping("/monthly-trends")
    public ResponseEntity<List<TrendResponse>> getMonthlyTrends(@RequestParam(required = false) Long userId,
                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends(userId, startDate, endDate));
    }

    @GetMapping("/weekly-trends")
    public ResponseEntity<List<WeeklyTrendResponse>> getWeeklyTrends(@RequestParam(required = false) Long userId,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.getWeeklyTrends(userId, startDate, endDate));
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<RecordResponse>> getRecentActivity() {
        return ResponseEntity.ok(dashboardService.getRecentActivity());
    }

}
