package com.socies.voto.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "load_test_results")
@Getter
@Setter
public class LoadTestResult extends BaseEntity {

    @Column(nullable = false)
    private LocalDate testDate;

    @Column(nullable = false)
    private String testType; // "LOAD" or "STRESS"

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private Integer concurrentUsers;

    @Column(nullable = false)
    private Integer totalRequests;

    @Column(nullable = false)
    private Integer successfulRequests;

    @Column(nullable = false)
    private Integer failedRequests;

    @Column(nullable = false)
    private Double averageResponseTime; // milliseconds

    @Column(nullable = false)
    private Double minResponseTime; // milliseconds

    @Column(nullable = false)
    private Double maxResponseTime; // milliseconds

    @Column(nullable = false)
    private Double throughput; // requests per second

    @Column(nullable = false)
    private LocalDateTime testStartTime;

    @Column(nullable = false)
    private LocalDateTime testEndTime;

    @Column(length = 500)
    private String notes;

    // Constructor vacío para JPA
    public LoadTestResult() {}

    // Constructor para crear resultado de prueba
    public LoadTestResult(
            String testType,
            String endpoint,
            Integer concurrentUsers,
            Integer totalRequests,
            Integer successfulRequests,
            Integer failedRequests,
            Double averageResponseTime,
            Double minResponseTime,
            Double maxResponseTime,
            Double throughput,
            LocalDateTime testStartTime,
            LocalDateTime testEndTime) {
        this.testDate = LocalDate.now();
        this.testType = testType;
        this.endpoint = endpoint;
        this.concurrentUsers = concurrentUsers;
        this.totalRequests = totalRequests;
        this.successfulRequests = successfulRequests;
        this.failedRequests = failedRequests;
        this.averageResponseTime = averageResponseTime;
        this.minResponseTime = minResponseTime;
        this.maxResponseTime = maxResponseTime;
        this.throughput = throughput;
        this.testStartTime = testStartTime;
        this.testEndTime = testEndTime;
    }
}
