package com.socies.voto.dtos.loadtesting;

import com.socies.voto.models.LoadTestResult;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadTestResultDTO {

    private Long id;
    private LocalDate testDate;
    private String testType;
    private String endpoint;
    private Integer concurrentUsers;
    private Integer totalRequests;
    private Integer successfulRequests;
    private Integer failedRequests;
    private Double averageResponseTime;
    private Double minResponseTime;
    private Double maxResponseTime;
    private Double throughput;
    private LocalDateTime testStartTime;
    private LocalDateTime testEndTime;
    private String notes;
    private Double successRate;

    // Constructor desde entidad
    public LoadTestResultDTO(LoadTestResult result) {
        this.id = result.getId();
        this.testDate = result.getTestDate();
        this.testType = result.getTestType();
        this.endpoint = result.getEndpoint();
        this.concurrentUsers = result.getConcurrentUsers();
        this.totalRequests = result.getTotalRequests();
        this.successfulRequests = result.getSuccessfulRequests();
        this.failedRequests = result.getFailedRequests();
        this.averageResponseTime = result.getAverageResponseTime();
        this.minResponseTime = result.getMinResponseTime();
        this.maxResponseTime = result.getMaxResponseTime();
        this.throughput = result.getThroughput();
        this.testStartTime = result.getTestStartTime();
        this.testEndTime = result.getTestEndTime();
        this.notes = result.getNotes();
        this.successRate = calculateSuccessRate();
    }

    private Double calculateSuccessRate() {
        if (totalRequests == null || totalRequests == 0) {
            return 0.0;
        }
        return (successfulRequests.doubleValue() / totalRequests.doubleValue()) * 100.0;
    }
}
