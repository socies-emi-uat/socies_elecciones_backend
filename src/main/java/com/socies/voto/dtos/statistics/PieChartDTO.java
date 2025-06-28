package com.socies.voto.dtos.statistics;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PieChartDTO extends BaseChartDTO {
    private List<Segment> datos;
    private Long totalVotos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Segment {
        private String etiqueta;
        private Long valor;
        private Double porcentaje;
        private String color; // Opcional para UI
    }

    public PieChartDTO(
            String titulo,
            String subtitulo,
            String descripcion,
            List<Segment> datos,
            Long totalVotos) {
        super(titulo, subtitulo, descripcion);
        this.datos = datos;
        this.totalVotos = totalVotos;
    }
}
