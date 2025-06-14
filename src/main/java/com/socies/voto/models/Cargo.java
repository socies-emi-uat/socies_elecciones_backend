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
public class Cargo extends BaseEntity {

    @Column(nullable = false, length = 20, unique = true)
    String nombre;

    String descripcion;

    public Cargo(String nombre_cargo, String descripcion) {

        this.nombre = nombre_cargo;
        this.descripcion = descripcion;
    }

    @OneToMany(
            mappedBy = "cargo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Candidato> candidatos;
}
