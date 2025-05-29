package com.socies.voto.services;
import com.socies.voto.dtos.usuario.UsuarioActivosDTO;
import com.socies.voto.dtos.usuario.UsuarioCreateDTO;
import com.socies.voto.dtos.usuario.UsuarioDTO;
import com.socies.voto.dtos.usuario.UsuarioUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.exceptions.Usuario.EmailAlreadyExistsException;
import com.socies.voto.exceptions.Usuario.UsuarioNotFoundException;
import com.socies.voto.models.Usuario;
import com.socies.voto.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<UsuarioDTO> getAllUsers() {
        return usuarioRepository.findAll().stream()
            .map(UsuarioDTO::new)
            .collect(Collectors.toList());
    }

    public UsuarioDTO getUserById(Long usuario_id) {
        return usuarioRepository.findById(usuario_id)
                .map(UsuarioDTO::new)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con el id: " + usuario_id));
    }

    public UsuarioDTO createUserEmpleado(UsuarioCreateDTO usuarioCreateVotanteDTO) {
        // Verificar si el correo ya existe

        if (usuarioRepository.existsByCorreo(usuarioCreateVotanteDTO.getCorreo())) { // si devuelve true entonces el usuario e email ya
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado.");
        }

        // Crear un nuevo usuario
        Usuario usuario = new Usuario(usuarioCreateVotanteDTO, encoder.encode(usuarioCreateVotanteDTO.getPassword()));
        usuario = usuarioRepository.save(usuario);

        // Guardar en la base de datos
        return new UsuarioDTO(usuario);
    }
    
    public void deleteUser(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException("El usuario con ID " + id + " no existe.");
        }
        usuarioRepository.deleteById(id);
    }

    public UsuarioActivosDTO getUsuarioActivos(Long id) {
        return usuarioRepository.findById(id).map(UsuarioActivosDTO::new).orElseThrow(() -> new ResourceNotFoundException("Recurso no encontrado"));
    }

    public UsuarioDTO updateUser(Long id, UsuarioUpdateDTO usuarioUpdateDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + id));

        usuario.setNombre(usuarioUpdateDTO.getNombre());
        usuario.setRol(usuarioUpdateDTO.getRol());
        usuario.setApellidoPaterno(usuarioUpdateDTO.getApellido_paterno());
        usuario.setApellidoMaterno(usuarioUpdateDTO.getApellido_materno());
        usuario.setCorreo(usuarioUpdateDTO.getCorreo());
        usuario.setCedulaIdentidad(usuarioUpdateDTO.getCarnet_identidad());

        usuario = usuarioRepository.save(usuario);

        return new UsuarioDTO(usuario);
    }

    public String disableOrEnableUser(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + id));
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

    // public UsuarioDTO updateFoto(Long id, MultipartFile foto) {}
}
