package com.socies.voto.repositories;

import com.socies.voto.models.Municipio;
import com.socies.voto.models.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    Optional<Municipio> findByNombre(String nombre);
}
