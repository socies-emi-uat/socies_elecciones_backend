package com.socies.voto.repositories;

import com.socies.voto.models.Partido;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
    Optional<Partido> findByNombrePartido(String nombrePartido);

    Optional<Partido> findBySigla(String sigla);

    List<Partido> findAllByEstadoTrue();
}
