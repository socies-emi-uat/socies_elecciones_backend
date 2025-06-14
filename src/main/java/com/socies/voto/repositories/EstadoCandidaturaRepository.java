package com.socies.voto.repositories;

import com.socies.voto.models.EstadoCandidatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoCandidaturaRepository extends JpaRepository<EstadoCandidatura, Long> {
    Optional<EstadoCandidatura> findByEstadoCandidatura(String estadoCandidatura);
}
