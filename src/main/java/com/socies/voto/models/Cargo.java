package com.socies.voto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cargo extends BaseEntity {

  @Column(nullable = false, length = 20)
  String nombre;

  String descripcion;

  public Cargo(String nombre_cargo, String descripcion) {

    this.nombre = nombre_cargo;
    this.descripcion = descripcion;
  }
}
