package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.loadtesting.DailyTestSummaryDTO;
import com.socies.voto.dtos.loadtesting.LoadTestConfigDTO;
import com.socies.voto.dtos.loadtesting.LoadTestResultDTO;
import com.socies.voto.services.LoadTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/administrador/load-testing")
@Tag(name = "Load Testing", description = "Endpoints para pruebas de carga y estrés")
public class LoadTestController {

    @Autowired
    private LoadTestService loadTestService;

    @PostMapping("/load-test")
    @Operation(summary = "Ejecutar prueba de carga", 
               description = "Ejecuta una prueba de carga en un endpoint específico")
    public ResponseEntity<?> executeLoadTest(@RequestBody LoadTestConfigDTO config) {
        try {
            if (!config.isValid()) {
                return ResponseEntity.badRequest()
                    .body("Configuración de prueba inválida. Verifique todos los campos.");
            }
            
            config.setTestType("LOAD");
            LoadTestResultDTO result = loadTestService.executeLoadTest(config);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error ejecutando prueba de carga: " + e.getMessage());
        }
    }

    @PostMapping("/stress-test")
    @Operation(summary = "Ejecutar prueba de estrés", 
               description = "Ejecuta una prueba de estrés en un endpoint específico")
    public ResponseEntity<?> executeStressTest(@RequestBody LoadTestConfigDTO config) {
        try {
            if (!config.isValid()) {
                return ResponseEntity.badRequest()
                    .body("Configuración de prueba inválida. Verifique todos los campos.");
            }
            
            config.setTestType("STRESS");
            LoadTestResultDTO result = loadTestService.executeStressTest(config);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error ejecutando prueba de estrés: " + e.getMessage());
        }
    }

    @GetMapping("/results/daily")
    @Operation(summary = "Obtener resultados por día", 
               description = "Obtiene todos los resultados de pruebas para una fecha específica")
    public ResponseEntity<List<LoadTestResultDTO>> getResultsByDate(
            @Parameter(description = "Fecha en formato YYYY-MM-DD")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<LoadTestResultDTO> results = loadTestService.getResultsByDate(date);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/results/daily/summary")
    @Operation(summary = "Obtener resumen diario", 
               description = "Obtiene un resumen de pruebas para una fecha y tipo específicos")
    public ResponseEntity<DailyTestSummaryDTO> getDailySummary(
            @Parameter(description = "Fecha en formato YYYY-MM-DD")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(description = "Tipo de prueba: LOAD o STRESS")
            @RequestParam String testType) {
        
        if (!testType.equals("LOAD") && !testType.equals("STRESS")) {
            return ResponseEntity.badRequest().build();
        }
        
        DailyTestSummaryDTO summary = loadTestService.getDailySummary(date, testType);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/results/range")
    @Operation(summary = "Obtener resultados por rango de fechas", 
               description = "Obtiene resúmenes diarios para un rango de fechas")
    public ResponseEntity<List<DailyTestSummaryDTO>> getResultsByDateRange(
            @Parameter(description = "Fecha inicio en formato YYYY-MM-DD")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Fecha fin en formato YYYY-MM-DD")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Tipo de prueba: LOAD o STRESS")
            @RequestParam String testType) {
        
        if (!testType.equals("LOAD") && !testType.equals("STRESS")) {
            return ResponseEntity.badRequest().build();
        }
        
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }
        
        List<DailyTestSummaryDTO> summaries = loadTestService.getResultsByDateRange(
            startDate, endDate, testType);
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/endpoints/test")
    @Operation(summary = "Obtener endpoints disponibles para testing", 
               description = "Lista los endpoints comunes disponibles para pruebas de carga")
    public ResponseEntity<List<String>> getTestableEndpoints() {
        List<String> endpoints = List.of(
            "/api/login",
            "/api/administrador/usuarios",
            "/api/administrador/candidatos",
            "/api/administrador/proceso-electoral",
            "/api/votante/proceso-electoral",
            "/api/votante/perfil",
            "/api/administrador/estadisticas"
        );
        
        return ResponseEntity.ok(endpoints);
    }
}