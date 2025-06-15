package com.socies.voto.repositories;

import com.socies.voto.models.Departamento;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByNombre(String nombre);
}
