package com.socies.voto.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class MetodoVoto extends BaseEntity{

    private String nombre;
    public MetodoVoto(String nombre) {
        this.nombre = nombre;
    }

    @OneToMany(mappedBy = "metodoVoto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Voto> votos;
}
