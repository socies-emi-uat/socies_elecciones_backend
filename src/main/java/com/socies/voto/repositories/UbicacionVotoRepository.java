package com.socies.voto.repositories;

import com.socies.voto.models.UbicacionVoto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UbicacionVotoRepository extends JpaRepository<UbicacionVoto, Long> {
    Optional<UbicacionVoto> findByNombreUbicacion(String nombreUbicacion);
}
