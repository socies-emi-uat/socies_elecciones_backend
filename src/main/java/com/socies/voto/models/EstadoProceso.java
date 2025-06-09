package com.socies.voto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
