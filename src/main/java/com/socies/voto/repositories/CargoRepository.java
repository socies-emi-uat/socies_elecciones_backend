package com.socies.voto.repositories;

import com.socies.voto.models.Cargo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    Optional<Cargo> findByNombre(String nombre_cargo);
}
