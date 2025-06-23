package com.socies.voto.repositories;

import com.socies.voto.models.Candidatura;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {
    boolean existsByNombreCandidaturaAndPartidoIdAndProcesoElectoralId(
            String nombreCandidatura, Long partidoId, Long procesoElectoralId);

    List<Candidatura> findByPartidoId(Long partidoId);

    List<Candidatura> findByProcesoElectoralId(Long procesoElectoralId);
}
