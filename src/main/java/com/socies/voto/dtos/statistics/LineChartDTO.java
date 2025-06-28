package com.socies.voto.dtos.statistics;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LineChartDTO extends BaseChartDTO {
    private List<DataPoint> datos;
    private String labelX;
    private String labelY;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataPoint {
        private String fecha;
        private Long valor;
        private String categoria; // Opcional para múltiples líneas
    }

    public LineChartDTO(
            String titulo,
            String subtitulo,
            String descripcion,
            List<DataPoint> datos,
            String labelX,
            String labelY) {
        super(titulo, subtitulo, descripcion);
        this.datos = datos;
        this.labelX = labelX;
        this.labelY = labelY;
    }
}
