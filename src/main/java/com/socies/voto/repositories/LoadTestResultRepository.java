package com.socies.voto.repositories;

import com.socies.voto.models.LoadTestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoadTestResultRepository extends JpaRepository<LoadTestResult, Long> {
    
    // Buscar resultados por fecha
    List<LoadTestResult> findByTestDate(LocalDate testDate);
    
    // Buscar resultados por tipo de prueba
    List<LoadTestResult> findByTestType(String testType);
    
    // Buscar resultados por fecha y tipo
    List<LoadTestResult> findByTestDateAndTestType(LocalDate testDate, String testType);
    
    // Buscar resultados por endpoint
    List<LoadTestResult> findByEndpoint(String endpoint);
    
    // Buscar resultados en un rango de fechas
    List<LoadTestResult> findByTestDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Buscar resultados por fecha y endpoint
    List<LoadTestResult> findByTestDateAndEndpoint(LocalDate testDate, String endpoint);
    
    // Obtener estadísticas promedio por día
    @Query("SELECT l.testDate, AVG(l.averageResponseTime), AVG(l.throughput), SUM(l.totalRequests) " +
           "FROM LoadTestResult l WHERE l.testType = :testType " +
           "GROUP BY l.testDate ORDER BY l.testDate DESC")
    List<Object[]> findDailyAveragesByTestType(@Param("testType") String testType);
    
    // Obtener las últimas pruebas por endpoint
    @Query("SELECT l FROM LoadTestResult l WHERE l.endpoint = :endpoint " +
           "ORDER BY l.testDate DESC, l.createdAt DESC")
    List<LoadTestResult> findLatestByEndpoint(@Param("endpoint") String endpoint);
}