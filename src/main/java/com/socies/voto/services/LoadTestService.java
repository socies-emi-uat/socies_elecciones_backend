package com.socies.voto.services;

import com.socies.voto.dtos.loadtesting.DailyTestSummaryDTO;
import com.socies.voto.dtos.loadtesting.LoadTestConfigDTO;
import com.socies.voto.dtos.loadtesting.LoadTestResultDTO;
import com.socies.voto.models.LoadTestResult;
import com.socies.voto.repositories.LoadTestResultRepository;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class LoadTestService {

    private static final Logger logger = LoggerFactory.getLogger(LoadTestService.class);
    
    @Autowired
    private LoadTestResultRepository loadTestResultRepository;
    
    @Value("${server.port:8080}")
    private int serverPort;
    
    @Value("${spring.application.name:socies}")
    private String applicationName;

    // Ejecutar prueba de carga
    public LoadTestResultDTO executeLoadTest(LoadTestConfigDTO config) {
        logger.info("Iniciando prueba de carga para endpoint: {}", config.getEndpoint());
        
        LocalDateTime startTime = LocalDateTime.now();
        List<Long> responseTimes = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(config.getConcurrentUsers());
        CountDownLatch latch = new CountDownLatch(config.getTotalRequests());
        
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        
        String baseUrl = "http://localhost:" + serverPort;
        String fullUrl = baseUrl + config.getEndpoint();
        
        try {
            // Ejecutar solicitudes concurrentes
            for (int i = 0; i < config.getTotalRequests(); i++) {
                executor.submit(() -> {
                    try {
                        long requestStart = System.currentTimeMillis();
                        boolean success = makeHttpRequest(fullUrl);
                        long requestEnd = System.currentTimeMillis();
                        
                        synchronized (responseTimes) {
                            responseTimes.add(requestEnd - requestStart);
                        }
                        
                        if (success) {
                            successCount.incrementAndGet();
                        } else {
                            failureCount.incrementAndGet();
                        }
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        logger.error("Error en solicitud: {}", e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }
            
            // Esperar a que terminen todas las solicitudes o timeout
            boolean completed = latch.await(config.getDurationSeconds(), TimeUnit.SECONDS);
            if (!completed) {
                logger.warn("Prueba de carga no completada en el tiempo especificado");
            }
            
        } catch (InterruptedException e) {
            logger.error("Prueba de carga interrumpida: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }
        
        LocalDateTime endTime = LocalDateTime.now();
        
        // Calcular métricas
        LoadTestResult result = calculateMetrics(
            config, startTime, endTime, responseTimes, 
            successCount.get(), failureCount.get()
        );
        
        // Guardar resultado
        LoadTestResult savedResult = loadTestResultRepository.save(result);
        
        logger.info("Prueba de carga completada. Éxito: {}, Fallos: {}", 
                   successCount.get(), failureCount.get());
        
        return new LoadTestResultDTO(savedResult);
    }
    
    // Ejecutar prueba de estrés (más agresiva)
    public LoadTestResultDTO executeStressTest(LoadTestConfigDTO config) {
        logger.info("Iniciando prueba de estrés para endpoint: {}", config.getEndpoint());
        
        // Para pruebas de estrés, aumentamos la concurrencia y carga
        LoadTestConfigDTO stressConfig = new LoadTestConfigDTO();
        stressConfig.setTestType("STRESS");
        stressConfig.setEndpoint(config.getEndpoint());
        stressConfig.setConcurrentUsers(config.getConcurrentUsers() * 2); // Duplicar usuarios
        stressConfig.setTotalRequests(config.getTotalRequests() * 3); // Triplicar solicitudes
        stressConfig.setDurationSeconds(config.getDurationSeconds());
        stressConfig.setNotes("Prueba de estrés - " + config.getNotes());
        
        return executeLoadTest(stressConfig);
    }
    
    // Realizar solicitud HTTP
    private boolean makeHttpRequest(String url) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            
            ClassicHttpResponse response = client.execute(request);
            int statusCode = response.getCode();
            
            return statusCode >= 200 && statusCode < 300;
            
        } catch (Exception e) {
            logger.debug("Error en solicitud HTTP a {}: {}", url, e.getMessage());
            return false;
        }
    }
    
    // Calcular métricas del test
    private LoadTestResult calculateMetrics(LoadTestConfigDTO config, LocalDateTime startTime, 
                                          LocalDateTime endTime, List<Long> responseTimes,
                                          int successCount, int failureCount) {
        
        if (responseTimes.isEmpty()) {
            responseTimes.add(0L); // Evitar división por cero
        }
        
        double avgResponseTime = responseTimes.stream()
                                            .mapToLong(Long::longValue)
                                            .average()
                                            .orElse(0.0);
        
        long minResponseTime = responseTimes.stream()
                                          .mapToLong(Long::longValue)
                                          .min()
                                          .orElse(0L);
        
        long maxResponseTime = responseTimes.stream()
                                          .mapToLong(Long::longValue)
                                          .max()
                                          .orElse(0L);
        
        long durationMillis = java.time.Duration.between(startTime, endTime).toMillis();
        double throughput = durationMillis > 0 ? (successCount * 1000.0) / durationMillis : 0.0;
        
        return new LoadTestResult(
            config.getTestType(),
            config.getEndpoint(),
            config.getConcurrentUsers(),
            config.getTotalRequests(),
            successCount,
            failureCount,
            avgResponseTime,
            (double) minResponseTime,
            (double) maxResponseTime,
            throughput,
            startTime,
            endTime
        );
    }
    
    // Obtener resultados por fecha
    public List<LoadTestResultDTO> getResultsByDate(LocalDate date) {
        return loadTestResultRepository.findByTestDate(date)
                .stream()
                .map(LoadTestResultDTO::new)
                .collect(Collectors.toList());
    }
    
    // Obtener resumen diario
    public DailyTestSummaryDTO getDailySummary(LocalDate date, String testType) {
        List<LoadTestResult> results = loadTestResultRepository.findByTestDateAndTestType(date, testType);
        List<LoadTestResultDTO> resultDTOs = results.stream()
                .map(LoadTestResultDTO::new)
                .collect(Collectors.toList());
        
        return new DailyTestSummaryDTO(date, testType, resultDTOs);
    }
    
    // Obtener resultados por rango de fechas
    public List<DailyTestSummaryDTO> getResultsByDateRange(LocalDate startDate, LocalDate endDate, String testType) {
        List<DailyTestSummaryDTO> summaries = new ArrayList<>();
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            DailyTestSummaryDTO summary = getDailySummary(currentDate, testType);
            if (summary.getTotalTests() > 0) {
                summaries.add(summary);
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return summaries;
    }
}