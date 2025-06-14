package com.socies.voto.repositories;

import com.socies.voto.models.ProcesoElectoral;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcesoElectoralRepository extends JpaRepository<ProcesoElectoral, Long> {
    Optional<ProcesoElectoral> findByNombreProceso(String nombreProceso);
}
