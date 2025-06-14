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
public class EstadoCandidato extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String estadoCandidato;

    public EstadoCandidato(String estadoCandidato) {
        this.estadoCandidato = estadoCandidato;
    }

    @OneToMany(
            mappedBy = "estado_candidato",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Candidato> candidatos;
}
