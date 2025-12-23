package com.socies.voto.services;

import com.socies.voto.dtos.loadtesting.LoadTestConfigDTO;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        name = "loadtest.scheduled.enabled",
        havingValue = "true",
        matchIfMissing = false)
public class ScheduledLoadTestService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledLoadTestService.class);

    @Autowired private LoadTestService loadTestService;

    // Lista de endpoints críticos para testing automático
    private final List<String> criticalEndpoints =
            Arrays.asList(
                    "/api/administrador/usuarios",
                    "/api/administrador/proceso-electoral",
                    "/api/votante/proceso-electoral");

    // Ejecutar pruebas de carga diarias a las 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void runDailyLoadTests() {
        logger.info("Iniciando pruebas de carga diarias programadas");

        for (String endpoint : criticalEndpoints) {
            try {
                LoadTestConfigDTO config =
                        new LoadTestConfigDTO(
                                "LOAD",
                                endpoint,
                                10, // 10 usuarios concurrentes
                                100, // 100 solicitudes totales
                                30, // 30 segundos de duración
                                "Prueba automática diaria");

                loadTestService.executeLoadTest(config);
                logger.info("Prueba de carga completada para endpoint: {}", endpoint);

                // Pausa entre pruebas para no sobrecargar el sistema
                Thread.sleep(5000);

            } catch (Exception e) {
                logger.error(
                        "Error en prueba de carga para endpoint {}: {}", endpoint, e.getMessage());
            }
        }

        logger.info("Pruebas de carga diarias completadas");
    }

    // Ejecutar pruebas de estrés semanales los domingos a las 3:00 AM
    @Scheduled(cron = "0 0 3 * * SUN")
    public void runWeeklyStressTests() {
        logger.info("Iniciando pruebas de estrés semanales programadas");

        for (String endpoint : criticalEndpoints) {
            try {
                LoadTestConfigDTO config =
                        new LoadTestConfigDTO(
                                "STRESS",
                                endpoint,
                                25, // 25 usuarios concurrentes
                                200, // 200 solicitudes totales
                                60, // 60 segundos de duración
                                "Prueba automática semanal de estrés");

                loadTestService.executeStressTest(config);
                logger.info("Prueba de estrés completada para endpoint: {}", endpoint);

                // Pausa más larga entre pruebas de estrés
                Thread.sleep(10000);

            } catch (Exception e) {
                logger.error(
                        "Error en prueba de estrés para endpoint {}: {}", endpoint, e.getMessage());
            }
        }

        logger.info("Pruebas de estrés semanales completadas");
    }
}
