package com.socies.voto.repositories;

import com.socies.voto.models.EstadoCandidatura;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoCandidaturaRepository extends JpaRepository<EstadoCandidatura, Long> {
    Optional<EstadoCandidatura> findByEstadoCandidatura(String estadoCandidatura);
}
