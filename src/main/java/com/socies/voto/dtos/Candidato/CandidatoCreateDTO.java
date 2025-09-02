package com.socies.voto.dtos.Candidato;

import com.socies.voto.security.validation.SafeText;
import com.socies.voto.security.validation.ValidCI;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CandidatoCreateDTO {
    @NotBlank(message = "El nombre del candidato es obligatorio")
    @Size(max = 20, message = "El nombre no puede exceder los 20 caracteres")
    @SafeText(message = "El nombre contiene caracteres no permitidos")
    String nombreCandidato;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 20, message = "El apellido paterno no puede exceder los 20 caracteres")
    @SafeText(message = "El apellido paterno contiene caracteres no permitidos")
    String apPaterno;

    @Size(max = 20, message = "El apellido materno no puede exceder los 20 caracteres")
    @SafeText(message = "El apellido materno contiene caracteres no permitidos")
    String apMaterno;

    @NotBlank(message = "La cédula de identidad es obligatoria")
    @Size(max = 10, message = "La cédula de identidad no puede exceder los 10 caracteres")
    @ValidCI(message = "Formato de cédula de identidad inválido")
    String ciCandidato;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    LocalDate fechaNacimiento;

    @NotBlank(message = "La URL de la foto es obligatoria")
    @Size(max = 45, message = "La URL de la foto no puede exceder los 45 caracteres")
    String fotoUrl;

    @Size(max = 45, message = "El correo electrónico no puede exceder los 45 caracteres")
    @Email(message = "Formato de correo electrónico inválido")
    String correoCandidato;

    @NotBlank(message = "La propuesta es obligatoria")
    @Size(max = 100, message = "La propuesta no puede exceder los 100 caracteres")
    @SafeText(message = "La propuesta contiene caracteres no permitidos")
    String propuesta;

    @NotNull(message = "El estado del candidato es obligatorio")
    Long estadoCandidatoId;

    @NotNull(message = "El cargo es obligatorio")
    Long cargoId;

    public CandidatoCreateDTO() {
        // Constructor vacío para permitir la creación de instancias sin parámetros
    }

    public CandidatoCreateDTO(
            String nombreCandidato,
            String apPaterno,
            String apMaterno,
            String ciCandidato,
            LocalDate fechaNacimiento,
            String fotoUrl,
            String correoCandidato,
            String propuesta,
            Long estadoCandidatoId,
            Long cargoId) {
        this.nombreCandidato = nombreCandidato;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.ciCandidato = ciCandidato;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoUrl = fotoUrl;
        this.correoCandidato = correoCandidato;
        this.propuesta = propuesta;
        this.estadoCandidatoId = estadoCandidatoId;
        this.cargoId = cargoId;
    }
}
