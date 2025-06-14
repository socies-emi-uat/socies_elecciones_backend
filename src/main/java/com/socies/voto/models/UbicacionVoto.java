package com.socies.voto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UbicacionVoto extends BaseEntity{

    @Column(name = "nombre_ubicacion", nullable = false)
    String nombreUbicacion;

    @Column(name = "descripcion_ubicacion", nullable = false)
    String descripcionUbicacion;


    float latitude = 0f;
    float longitude = 0f;

    @Column(nullable = false)
    String direccion;

    public UbicacionVoto(String nombreUbicacion, String descripcionUbicacion, float latitude, float longitude, String direccion) {
        this.nombreUbicacion = nombreUbicacion;
        this.descripcionUbicacion = descripcionUbicacion;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direccion = direccion;
    }

    public UbicacionVoto(String nombreUbicacion, String descripcionUbicacion, String direccion) {
        this.nombreUbicacion = nombreUbicacion;
        this.descripcionUbicacion = descripcionUbicacion;
        this.direccion = direccion;
    }

    @ManyToOne
    @JoinColumn(name = "municipio_id", referencedColumnName = "id", nullable = false)
    private Municipio municipio;

}
