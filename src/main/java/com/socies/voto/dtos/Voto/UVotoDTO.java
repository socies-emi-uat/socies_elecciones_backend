package com.socies.voto.dtos.Voto;

import com.socies.voto.dtos.Candidatura.CandidaturaDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoDTO;
import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralDTO;
import com.socies.voto.dtos.UbicacionVoto.UbicacionVotoDTO;
import com.socies.voto.dtos.usuario.UsuarioDTO;
import com.socies.voto.models.Voto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UVotoDTO {
    Long id;
    String hashVotacion;
    ProcesoElectoralDTO procesoElectoral;
    CandidaturaDTO candidatura;
    MetodoVotoDTO metodoVoto;
    UbicacionVotoDTO ubicacionVoto;

    public UVotoDTO(Voto voto) {
        this.id = voto.getId();
        this.hashVotacion = voto.getHashVotacion();
        this.procesoElectoral = new ProcesoElectoralDTO(voto.getProcesoElectoral());
        this.candidatura = new CandidaturaDTO(voto.getCandidatura());
        this.metodoVoto = new MetodoVotoDTO(voto.getMetodoVoto());
        this.ubicacionVoto  = new UbicacionVotoDTO(voto.getUbicacionVoto());
    }
}
