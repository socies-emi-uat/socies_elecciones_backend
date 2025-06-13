package com.socies.voto.dtos.Cargo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CargoCreateDTO {

    @NotBlank(message = "El nombre del cargo es obligatorio")
    @Size(max = 20, message = "El nombre no puede exceder los 20 caracteres")
    String nombre_cargo;

    @Size(max = 100, message = "La descripción no puede exceder los 100 caracteres")
    String descripcion;

    public CargoCreateDTO(Long id, String nombreCargo, String descripcion) {
        this.nombre_cargo = nombreCargo;
        this.descripcion = descripcion;
    }
}
