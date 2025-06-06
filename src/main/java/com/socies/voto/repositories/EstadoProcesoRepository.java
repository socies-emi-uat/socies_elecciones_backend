package com.socies.voto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socies.voto.models.EstadoProceso;

public interface EstadoProcesoRepository extends JpaRepository<EstadoProceso, Long> {    
    Optional<EstadoProceso> findByEstadoProceso(String estadoProceso);
}
