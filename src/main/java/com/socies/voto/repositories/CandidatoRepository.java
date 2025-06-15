package com.socies.voto.repositories;

import com.socies.voto.models.Candidato;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    // fundamental para verificar que no haya otro candidato con la misma cédula
    Optional<Candidato> findByCedulaIdentidad(String cedulaIdentidad);

    Optional<Candidato> findByCorreo(String correo); // opcional
}
