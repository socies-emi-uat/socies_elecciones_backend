package com.socies.voto.dtos.statistics;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralStatsDTO {
    private Long totalVotos;
    private Long totalCandidatos;
    private Long totalUbicaciones;
    private Double participacion; // Porcentaje si tienes total de electores
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String candidatoGanador;
    private Long votosGanador;
}
