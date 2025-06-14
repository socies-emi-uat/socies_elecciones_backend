package com.socies.voto.models;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
            mappedBy = "estadoCandidato",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Candidato> candidatos;
}
