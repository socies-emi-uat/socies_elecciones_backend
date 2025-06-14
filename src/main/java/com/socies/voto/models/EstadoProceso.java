package com.socies.voto.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EstadoProceso extends BaseEntity {

    @Column(name = "estado_proceso", nullable = false, length = 20)
    String estadoProceso;

    public EstadoProceso(String estado) {
        this.estadoProceso = estado;
    }

    @OneToMany(
            mappedBy = "estado_proceso",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<ProcesoElectoral> procesoElectorals;

}
