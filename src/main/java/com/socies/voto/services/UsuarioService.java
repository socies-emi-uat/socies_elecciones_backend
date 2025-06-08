package com.socies.voto.services;

import com.socies.voto.dtos.usuario.*;
import com.socies.voto.exceptions.Rol.RolNotFoundException;
import com.socies.voto.exceptions.Usuario.EmailAlreadyExistsException;
import com.socies.voto.exceptions.Usuario.UsuarioNotFoundException;
import com.socies.voto.models.Rol;
import com.socies.voto.models.Usuario;
import com.socies.voto.repositories.RolRepository;
import com.socies.voto.repositories.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

  @Autowired private UsuarioRepository usuarioRepository;

  @Autowired private RolRepository rolRepository;

  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

  public List<UsuarioDTO> getAllUsers() {
    return usuarioRepository.findAll().stream().map(UsuarioDTO::new).collect(Collectors.toList());
  }

  public UsuarioDTO getUserById(Long usuario_id) {
    return usuarioRepository
        .findById(usuario_id)
        .map(UsuarioDTO::new)
        .orElseThrow(
            () -> new UsuarioNotFoundException("Usuario no encontrado con el id: " + usuario_id));
  }

  public UsuarioDTO createUsuario(UsuarioCreateDTO usuarioCreateVotanteDTO) {
    // Verificar si el correo ya existe

    if (usuarioRepository.existsByCorreo(
        usuarioCreateVotanteDTO.getCorreo())) { // si devuelve true entonces el usuario e email ya
      throw new EmailAlreadyExistsException("El correo electrónico ya está registrado.");
    }

    Rol rol_usuario =
        rolRepository
            .findById(usuarioCreateVotanteDTO.getRol_id())
            .orElseThrow(() -> new RolNotFoundException("Rol no encontrado."));

    // Crear un nuevo usuario
    Usuario usuario =
        new Usuario(
            usuarioCreateVotanteDTO,
            encoder.encode(usuarioCreateVotanteDTO.getPassword()),
            rol_usuario);
    usuario = usuarioRepository.save(usuario);

    // Guardar en la base de datos
    return new UsuarioDTO(usuario);
  }

  public String disableOrEnableUser(Long id) {
    Usuario usuario =
        usuarioRepository
            .findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + id));
    String estado_nuevo = null;
    if (usuario.is_deleted()) {
      usuario.set_deleted(false);
      estado_nuevo = "Habilitado";
    } else {
      usuario.set_deleted(true);
      estado_nuevo = "Desabilitado";
    }

    usuarioRepository.save(usuario);
    return estado_nuevo;
  }
}
