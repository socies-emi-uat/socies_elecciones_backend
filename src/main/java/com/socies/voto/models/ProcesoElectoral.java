package com.socies.voto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class ProcesoElectoral extends BaseEntity {

    @Column(name = "nombre_proceso", nullable = false, length = 20)
    String nombreProceso;

    @Column(name = "descripcion_proceso", nullable = false, length = 50)
    String descripcionProceso;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    public ProcesoElectoral(ProcesoElectoral procesoElectoral) {
        this.nombreProceso = procesoElectoral.getNombreProceso();
        this.descripcionProceso = procesoElectoral.getDescripcionProceso();
        this.fechaInicio = procesoElectoral.getFechaInicio();
        this.fechaFin = procesoElectoral.getFechaFin();
    }
}
