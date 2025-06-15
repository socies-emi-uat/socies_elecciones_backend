package com.socies.voto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socies.voto.models.MetodoVoto;

public interface MetodoVotoRepository extends JpaRepository<MetodoVoto, Long> {
    Optional<MetodoVoto> findByNombre(String nombre);
}
