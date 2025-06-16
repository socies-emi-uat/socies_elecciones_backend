package com.socies.voto.repositories;

import com.socies.voto.models.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByUsuarioIdAndProcesoElectoralId(Long usuarioId, Long procesoElectoralId);
}
