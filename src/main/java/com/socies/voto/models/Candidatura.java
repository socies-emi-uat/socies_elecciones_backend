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
public class Candidatura extends BaseEntity {
    @Column(name = "nombre_candidatura", nullable = false, length = 20)
    private String nombreCandidatura;
    private String lema;

    @ManyToOne
    @JoinColumn(name = "candidato_id", referencedColumnName = "id", nullable = false)
    private Candidato candidato;

    @ManyToOne
    @JoinColumn(name = "partido_id", referencedColumnName = "id", nullable = false)
    private Partido partido;

    @ManyToOne
    @JoinColumn(name = "estado_candidatura_id", referencedColumnName = "id", nullable = false)
    private EstadoCandidatura estadoCandidatura;

    @ManyToOne
    @JoinColumn(name = "proceso_electoral_id", referencedColumnName = "id", nullable = false)
    private ProcesoElectoral procesoElectoral;

    public Candidatura(Candidatura candidatura) {
        this.nombreCandidatura = candidatura.getNombreCandidatura();
        this.lema = candidatura.getLema();
        this.candidato = candidatura.getCandidato();
        this.partido = candidatura.getPartido();
        this.estadoCandidatura = candidatura.getEstadoCandidatura();
        this.procesoElectoral = candidatura.getProcesoElectoral();
    }
}
