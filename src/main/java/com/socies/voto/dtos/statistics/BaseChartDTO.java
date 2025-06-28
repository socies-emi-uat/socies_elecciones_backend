package com.socies.voto.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseChartDTO {
    private String titulo;
    private String subtitulo;
    private String descripcion;
}
