package com.socies.voto.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


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

    @ManyToOne
    @JoinColumn(name = "estado_proceso_id", referencedColumnName = "id", nullable = false)
    private EstadoProceso estadoProceso;

    @OneToMany(
            mappedBy = "procesoElectoral",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Candidatura> candidaturas;
}
