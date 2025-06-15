package com.socies.voto.repositories;

import com.socies.voto.models.UbicacionVoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UbicacionVotoRepository extends JpaRepository<UbicacionVoto, Long> {
    Optional<UbicacionVoto> findByNombreUbicacion(String nombreUbicacion);
}
