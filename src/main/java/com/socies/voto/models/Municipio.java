package com.socies.voto.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Municipio extends BaseEntity{
    private String nombre;
    public Municipio(Municipio municipio) {
        this.nombre = municipio.getNombre();
    }
    @ManyToOne
    @JoinColumn(name = "provincia_id", referencedColumnName = "id", nullable = false)
    private Provincia provincia;

    @OneToMany(mappedBy = "municipio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    List<UbicacionVoto> ubicacionVotos;
}
