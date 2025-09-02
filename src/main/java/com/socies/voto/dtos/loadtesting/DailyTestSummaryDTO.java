package com.socies.voto.dtos.loadtesting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTestSummaryDTO {
    
    private LocalDate date;
    private String testType;
    private Integer totalTests;
    private Double averageResponseTime;
    private Double averageThroughput;
    private Integer totalRequests;
    private Double overallSuccessRate;
    private List<LoadTestResultDTO> tests;
    
    // Constructor para resumen diario
    public DailyTestSummaryDTO(LocalDate date, String testType, List<LoadTestResultDTO> tests) {
        this.date = date;
        this.testType = testType;
        this.tests = tests;
        this.totalTests = tests.size();
        calculateAverages();
    }
    
    private void calculateAverages() {
        if (tests == null || tests.isEmpty()) {
            averageResponseTime = 0.0;
            averageThroughput = 0.0;
            totalRequests = 0;
            overallSuccessRate = 0.0;
            return;
        }
        
        double sumResponseTime = 0.0;
        double sumThroughput = 0.0;
        int sumTotalRequests = 0;
        int sumSuccessfulRequests = 0;
        
        for (LoadTestResultDTO test : tests) {
            sumResponseTime += test.getAverageResponseTime();
            sumThroughput += test.getThroughput();
            sumTotalRequests += test.getTotalRequests();
            sumSuccessfulRequests += test.getSuccessfulRequests();
        }
        
        averageResponseTime = sumResponseTime / tests.size();
        averageThroughput = sumThroughput / tests.size();
        totalRequests = sumTotalRequests;
        
        if (sumTotalRequests > 0) {
            overallSuccessRate = (sumSuccessfulRequests * 100.0) / sumTotalRequests;
        } else {
            overallSuccessRate = 0.0;
        }
    }
}