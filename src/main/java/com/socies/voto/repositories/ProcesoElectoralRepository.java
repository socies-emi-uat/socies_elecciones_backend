package com.socies.voto.repositories;

import com.socies.voto.models.ProcesoElectoral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcesoElectoralRepository extends JpaRepository<ProcesoElectoral, Long> {
    Optional<ProcesoElectoral> findByNombreProceso(String nombreProceso);
}

