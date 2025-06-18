package com.socies.voto.services;

import com.socies.voto.dtos.usuario.UsuarioCreateDTO;
import com.socies.voto.dtos.usuario.UsuarioDTO;
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
        return usuarioRepository.findAll().stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    public UsuarioDTO getUserById(Long usuario_id) {
        return usuarioRepository
                .findById(usuario_id)
                .map(UsuarioDTO::new)
                .orElseThrow(
                        () ->
                                new UsuarioNotFoundException(
                                        "Usuario no encontrado con el id: " + usuario_id));
    }

    public UsuarioDTO createUsuario(UsuarioCreateDTO usuarioCreateVotanteDTO) {
        String correo = usuarioCreateVotanteDTO.getCorreo();
        String cedula = usuarioCreateVotanteDTO.getCedulaIdentidad();

        // Validar formato de correo
        if (correo != null
                && !correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }

        // Validar duplicado por correo
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado.");
        }

        // ✅ Validar duplicado por cédula de identidad
        if (usuarioRepository.existsByCedulaIdentidad(cedula)) {
            throw new IllegalArgumentException("La cédula de identidad ya está registrada.");
        }

        Rol rol_usuario =
                rolRepository
                        .findById(usuarioCreateVotanteDTO.getRol_id())
                        .orElseThrow(() -> new RolNotFoundException("Rol no encontrado."));

        Usuario usuario =
                new Usuario(
                        usuarioCreateVotanteDTO,
                        encoder.encode(usuarioCreateVotanteDTO.getPassword()),
                        rol_usuario);

        usuario = usuarioRepository.save(usuario);

        return new UsuarioDTO(usuario);
    }

    public String disableOrEnableUser(Long id) {
        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new UsuarioNotFoundException(
                                                "Usuario no encontrado con ID: " + id));

        String estado_nuevo;
        if (usuario.is_deleted()) {
            usuario.set_deleted(false);
            estado_nuevo = "Deshabilitado";
        } else {
            usuario.set_deleted(true);
            estado_nuevo = "Habilitado";
        }

        usuarioRepository.save(usuario);
        return estado_nuevo;
    }
}
