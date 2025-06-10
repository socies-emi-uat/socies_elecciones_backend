package com.socies.voto.repositories;

import com.socies.voto.models.EstadoCandidato;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoCandidatoRepository extends JpaRepository<EstadoCandidato, Long> {
    Optional<EstadoCandidato> findByEstadoCandidato(String estadoCandidato);
}
