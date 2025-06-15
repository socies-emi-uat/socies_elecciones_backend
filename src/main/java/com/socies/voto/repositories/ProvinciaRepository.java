package com.socies.voto.repositories;

import com.socies.voto.models.Provincia;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    Optional<Provincia> findByNombre(String nombre);
}
