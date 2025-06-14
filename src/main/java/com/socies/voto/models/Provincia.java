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
public class Provincia extends BaseEntity {
    private String nombre;

    public Provincia(Provincia provincia) {
        this.nombre = provincia.getNombre();
    }

    @ManyToOne
    @JoinColumn(name = "departamento_id", referencedColumnName = "id", nullable = false)
    private Departamento departamento;

    @OneToMany(
            mappedBy = "provincia",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<Municipio> municipios;
}
