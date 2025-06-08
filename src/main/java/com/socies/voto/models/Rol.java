package com.socies.voto.models;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol extends BaseEntity {

  @Column(nullable = false)
  private String tipo_rol;

  @OneToMany(
      mappedBy = "rol",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<Usuario> usuarios;

  public Rol(String tipo_rol) {
    this.tipo_rol = tipo_rol;
  }
}
