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
public class EstadoCandidatura extends BaseEntity {

    @Column(name = "estado_candidatura", nullable = false, length = 20)
    String estadoCandidatura;

    public EstadoCandidatura(String estado) {
        this.estadoCandidatura = estado;
    }

    @OneToMany(
            mappedBy = "estadoCandidatura",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Candidatura> candidaturas;


}
