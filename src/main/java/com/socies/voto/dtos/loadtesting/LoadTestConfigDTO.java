package com.socies.voto.dtos.loadtesting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadTestConfigDTO {

    private String testType; // "LOAD" or "STRESS"
    private String endpoint;
    private Integer concurrentUsers;
    private Integer totalRequests;
    private Integer durationSeconds;
    private String notes;

    // Validar configuración
    public boolean isValid() {
        return testType != null
                && (testType.equals("LOAD") || testType.equals("STRESS"))
                && endpoint != null
                && !endpoint.trim().isEmpty()
                && concurrentUsers != null
                && concurrentUsers > 0
                && totalRequests != null
                && totalRequests > 0
                && durationSeconds != null
                && durationSeconds > 0;
    }
}
