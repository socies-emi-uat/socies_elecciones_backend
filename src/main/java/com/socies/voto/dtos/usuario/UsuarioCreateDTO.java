package com.socies.voto.dtos.usuario;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateDTO {

    @NonNull private String nombre;

    @NonNull private String apellidoPaterno;

    private String apellidoMaterno;

    private String password;

    @NonNull private String cedulaIdentidad;

    @NonNull private LocalDateTime fechaNacimiento;

    private String correo;

    @NonNull private String telefono;

    @NonNull private String celular;

    @NonNull private Long rol_id;
}
