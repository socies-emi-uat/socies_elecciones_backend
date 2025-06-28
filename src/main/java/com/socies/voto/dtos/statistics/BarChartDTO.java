package com.socies.voto.dtos.statistics;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BarChartDTO extends BaseChartDTO {
    private List<BarData> datos;
    private String labelX;
    private String labelY;
    private Boolean horizontal;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BarData {
        private String etiqueta;
        private Long valor;
        private String color; // Opcional
    }

    public BarChartDTO(
            String titulo,
            String subtitulo,
            String descripcion,
            List<BarData> datos,
            String labelX,
            String labelY,
            Boolean horizontal) {
        super(titulo, subtitulo, descripcion);
        this.datos = datos;
        this.labelX = labelX;
        this.labelY = labelY;
        this.horizontal = horizontal != null ? horizontal : false;
    }
}
