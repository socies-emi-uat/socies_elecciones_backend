package com.socies.voto.repositories;

import com.socies.voto.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCorreo(String correo);

    Optional<Usuario> getUsuarioByCorreo(String correo);
    Optional<Usuario> getUsuarioByCedulaIdentidad(String cedulaIdentidad);

}

