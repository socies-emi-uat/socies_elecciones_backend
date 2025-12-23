package com.socies.voto;

import static org.junit.jupiter.api.Assertions.*;

import com.socies.voto.dtos.loadtesting.LoadTestConfigDTO;
import com.socies.voto.dtos.loadtesting.LoadTestResultDTO;
import com.socies.voto.models.LoadTestResult;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class LoadTestServiceTest {

    @Test
    void testLoadTestConfigValidation() {
        // Test valid config
        LoadTestConfigDTO validConfig =
                new LoadTestConfigDTO("LOAD", "/api/test", 5, 50, 30, "Test config");
        assertTrue(validConfig.isValid());

        // Test invalid config - missing testType
        LoadTestConfigDTO invalidConfig1 =
                new LoadTestConfigDTO(null, "/api/test", 5, 50, 30, "Test config");
        assertFalse(invalidConfig1.isValid());

        // Test invalid config - invalid testType
        LoadTestConfigDTO invalidConfig2 =
                new LoadTestConfigDTO("INVALID", "/api/test", 5, 50, 30, "Test config");
        assertFalse(invalidConfig2.isValid());

        // Test invalid config - zero concurrent users
        LoadTestConfigDTO invalidConfig3 =
                new LoadTestConfigDTO("LOAD", "/api/test", 0, 50, 30, "Test config");
        assertFalse(invalidConfig3.isValid());
    }

    @Test
    void testLoadTestResultCreation() {
        // Create test result
        LoadTestResult result =
                new LoadTestResult(
                        "LOAD",
                        "/api/test",
                        5,
                        100,
                        95,
                        5,
                        250.0,
                        100.0,
                        500.0,
                        3.8,
                        LocalDateTime.now().minusMinutes(5),
                        LocalDateTime.now());

        assertNotNull(result);
        assertEquals("LOAD", result.getTestType());
        assertEquals("/api/test", result.getEndpoint());
        assertEquals(5, result.getConcurrentUsers());
        assertEquals(100, result.getTotalRequests());
        assertEquals(95, result.getSuccessfulRequests());
        assertEquals(5, result.getFailedRequests());
    }

    @Test
    void testLoadTestResultDTO() {
        LoadTestResult result =
                new LoadTestResult(
                        "LOAD",
                        "/api/test",
                        5,
                        100,
                        95,
                        5,
                        250.0,
                        100.0,
                        500.0,
                        3.8,
                        LocalDateTime.now().minusMinutes(5),
                        LocalDateTime.now());

        LoadTestResultDTO dto = new LoadTestResultDTO(result);

        assertEquals("LOAD", dto.getTestType());
        assertEquals("/api/test", dto.getEndpoint());
        assertEquals(5, dto.getConcurrentUsers());
        assertEquals(100, dto.getTotalRequests());
        assertEquals(95, dto.getSuccessfulRequests());
        assertEquals(5, dto.getFailedRequests());
        assertEquals(95.0, dto.getSuccessRate(), 0.1);
    }

    @Test
    void testSuccessRateCalculation() {
        // Test 100% success rate
        LoadTestResult perfectResult =
                new LoadTestResult(
                        "LOAD",
                        "/api/test",
                        5,
                        100,
                        100,
                        0,
                        250.0,
                        100.0,
                        500.0,
                        3.8,
                        LocalDateTime.now().minusMinutes(5),
                        LocalDateTime.now());
        LoadTestResultDTO perfectDTO = new LoadTestResultDTO(perfectResult);
        assertEquals(100.0, perfectDTO.getSuccessRate(), 0.1);

        // Test 0% success rate
        LoadTestResult failedResult =
                new LoadTestResult(
                        "LOAD",
                        "/api/test",
                        5,
                        100,
                        0,
                        100,
                        250.0,
                        100.0,
                        500.0,
                        0.0,
                        LocalDateTime.now().minusMinutes(5),
                        LocalDateTime.now());
        LoadTestResultDTO failedDTO = new LoadTestResultDTO(failedResult);
        assertEquals(0.0, failedDTO.getSuccessRate(), 0.1);

        // Test 75% success rate
        LoadTestResult partialResult =
                new LoadTestResult(
                        "LOAD",
                        "/api/test",
                        5,
                        100,
                        75,
                        25,
                        250.0,
                        100.0,
                        500.0,
                        2.5,
                        LocalDateTime.now().minusMinutes(5),
                        LocalDateTime.now());
        LoadTestResultDTO partialDTO = new LoadTestResultDTO(partialResult);
        assertEquals(75.0, partialDTO.getSuccessRate(), 0.1);
    }
}
