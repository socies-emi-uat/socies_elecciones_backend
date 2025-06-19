package com.socies.voto.repositories;

import com.socies.voto.models.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCorreo(String correo);

    boolean existsByCedulaIdentidad(String cedulaIdentidad);

    Optional<Usuario> getUsuarioByCorreo(String correo);

    Optional<Usuario> getUsuarioByCedulaIdentidad(String cedulaIdentidad);
}
