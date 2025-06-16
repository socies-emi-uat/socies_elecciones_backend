package com.socies.voto.dtos.Voto;

import com.socies.voto.models.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UVotoCreateDTO {
    ProcesoElectoral procesoElectoral; // proceso, ejemplo elecciones nacionales
    Candidatura candidatura; // ejemplo, candidato.
    MetodoVoto metodoVoto; // por defecto es electronico.
    UbicacionVoto ubicacionVoto; // por defecto es internet

    public UVotoCreateDTO(Voto voto) {
        this.procesoElectoral = voto.getProcesoElectoral(); // ¿de donde viene?
        this.candidatura = voto.getCandidatura();
        this.metodoVoto = voto.getMetodoVoto();
        this.ubicacionVoto = voto.getUbicacionVoto();
    }
}
