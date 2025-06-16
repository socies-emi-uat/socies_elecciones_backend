package com.socies.voto.dtos.Candidato;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CandidatoUpdateDTO {
    @Size(max = 20, message = "El nombre no puede exceder los 20 caracteres")
    String nombreCandidato;

    @Size(max = 20, message = "El apellido paterno no puede exceder los 20 caracteres")
    String apPaterno;

    @Size(max = 20, message = "El apellido materno no puede exceder los 20 caracteres")
    String apMaterno;

    @Size(max = 10, message = "La cédula de identidad no puede exceder los 10 caracteres")
    String ciCandidato;

    LocalDate fechaNacimiento;

    @Size(max = 45, message = "La URL de la foto no puede exceder los 45 caracteres")
    String fotoUrl;

    @Size(max = 45, message = "El correo electrónico no puede exceder los 45 caracteres")
    String correoCandidato;

    @Size(max = 100, message = "La propuesta no puede exceder los 100 caracteres")
    String propuesta;

    Long estadoCandidatoId;
    Long cargoId;
}
