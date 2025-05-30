package com.socies.voto.repositories;

import com.socies.voto.models.Rol;
import com.socies.voto.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
}

