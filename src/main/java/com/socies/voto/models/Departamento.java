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
public class Departamento extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String nombre;

    public Departamento(Departamento departamento) {
        this.nombre = departamento.getNombre();
    }

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Provincia> provincias;
}
