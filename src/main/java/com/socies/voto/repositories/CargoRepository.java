package com.socies.voto.repositories;

import com.socies.voto.models.Cargo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    Optional<Cargo> findByNombre(String nombre_cargo);

    // Nuevos métodos para búsqueda filtrada
    List<Cargo> findByNombreContainingIgnoreCase(String nombre);

    List<Cargo> findByDescripcionContainingIgnoreCase(String descripcion);

    List<Cargo> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
            String nombre, String descripcion);
}
