package com.socies.voto.repositories;

import com.socies.voto.models.Municipio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    Optional<Municipio> findByNombre(String nombre);
}
