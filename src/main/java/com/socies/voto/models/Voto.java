package com.socies.voto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Voto extends BaseEntity{
    @Column(nullable = false, columnDefinition = "TEXT")
    String hashVotacion;

    public Voto(Voto voto) {
        this.hashVotacion = voto.getHashVotacion();
        this.usuario = voto.getUsuario();
        this.procesoElectoral = voto.getProcesoElectoral();
        this.candidatura = voto.getCandidatura();
        this.metodoVoto = voto.getMetodoVoto();
        this.ubicacionVoto = voto.getUbicacionVoto();

    }

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "proceso_id", referencedColumnName = "id", nullable = false)
    private ProcesoElectoral procesoElectoral;

    @ManyToOne
    @JoinColumn(name = "candidatura_id", referencedColumnName = "id", nullable = false)
    private Candidatura candidatura;

    @ManyToOne
    @JoinColumn(name = "metodo_voto_id", referencedColumnName = "id", nullable = false)
    private MetodoVoto metodoVoto;

    @ManyToOne
    @JoinColumn(name = "ubicacion_voto_id", referencedColumnName = "id", nullable = false)
    private UbicacionVoto ubicacionVoto;
}
