package com.socies.voto.models;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Municipio extends BaseEntity {
    private String nombre;

    public Municipio(Municipio municipio) {
        this.nombre = municipio.getNombre();
    }

    @ManyToOne
    @JoinColumn(name = "provincia_id", referencedColumnName = "id", nullable = false)
    private Provincia provincia;

    @OneToMany(
            mappedBy = "municipio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<UbicacionVoto> ubicacionVotos;
}
