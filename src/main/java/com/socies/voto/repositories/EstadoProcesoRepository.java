package com.socies.voto.repositories;

import com.socies.voto.models.EstadoProceso;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoProcesoRepository extends JpaRepository<EstadoProceso, Long> {
  Optional<EstadoProceso> findByEstadoProceso(String estadoProceso);
}
