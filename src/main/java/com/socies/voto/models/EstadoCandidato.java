
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
public class EstadoCandidato extends BaseEntity {

    @Column( nullable = false, length = 20)
    private String estado_candidato;

/*    @OneToMany(
            mappedBy = "estadoCandidato",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Candidato> candidatos;
 */
    public EstadoCandidato(String estadoCandidato) {
        this.estado_candidato = estadoCandidato;
    }


}
