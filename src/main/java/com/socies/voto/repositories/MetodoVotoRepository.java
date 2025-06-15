package com.socies.voto.repositories;

import com.socies.voto.models.MetodoVoto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetodoVotoRepository extends JpaRepository<MetodoVoto, Long> {
    Optional<MetodoVoto> findByNombre(String nombre);
}
