package com.socies.voto.services;

import com.socies.voto.dtos.Voto.AVotoDTO;
import com.socies.voto.dtos.Voto.UVotoCreateDTO;
import com.socies.voto.dtos.Voto.UVotoDTO;
import com.socies.voto.dtos.usuario.UsuarioPrincipalDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.MetodoVoto;
import com.socies.voto.models.UbicacionVoto;
import com.socies.voto.models.Usuario;
import com.socies.voto.models.Voto;
import com.socies.voto.repositories.MetodoVotoRepository;
import com.socies.voto.repositories.UbicacionVotoRepository;
import com.socies.voto.repositories.UsuarioRepository;
import com.socies.voto.repositories.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MetodoVotoRepository metodoVotoRepository;

    @Autowired
    private UbicacionVotoRepository ubicacionVotoRepository;

    // separar en servicio de administracion y servicios de usuario

    // ADMINISTRADOR
    public List<AVotoDTO> findAllAdmin() {
        return votoRepository.findAll().stream().map(AVotoDTO::new).collect(Collectors.toList());
    }

    // VOTANTES
    public List<UVotoDTO> findAllVotantes() {
        return votoRepository.findAll().stream().map(UVotoDTO::new).collect(Collectors.toList());
    }

    // ADMINISTRADOR
    public AVotoDTO findByIdAdministrador(Long id) {
        return votoRepository.findById(id).map(AVotoDTO::new).orElse(null);
    }

    // VOTANTE

    public UVotoDTO findByIdVotante(Long id) {
        return votoRepository.findById(id).map(UVotoDTO::new).orElse(null);
    }

    // VOTANTE
    public UVotoDTO save(UVotoCreateDTO uVotoCreateDTO) {
        UsuarioPrincipalDTO usuarioActuador = (UsuarioPrincipalDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Verificar si el usuario tiene permisos para votar
        if (!usuarioActuador.puedeVotar()) {
            throw new ResourceNotFoundException("El usuario no puede votar");
        }

        // Verificar si el usuario ya votó en este proceso electoral
        Long usuarioId = usuarioActuador.getId();
        Long procesoElectoralId = uVotoCreateDTO.getProcesoElectoral().getId();

        boolean yaVoto = votoRepository.existsByUsuarioIdAndProcesoElectoralId(usuarioId, procesoElectoralId);
        if (yaVoto) {
            throw new IllegalStateException("El usuario ya ha emitido su voto en este proceso electoral.");
        }

        // Recuperar datos estáticos
        UbicacionVoto ubicacionVoto = ubicacionVotoRepository.findById(uVotoCreateDTO.getUbicacionVoto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación de voto no registrada."));

        MetodoVoto metodoVoto = metodoVotoRepository.findById(uVotoCreateDTO.getMetodoVoto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Método de voto no encontrado."));

        // Cargar entidad usuario completa
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        // Crear voto
        Voto voto = new Voto();
        voto.setUsuario(usuario);
        voto.setMetodoVoto(metodoVoto);
        voto.setUbicacionVoto(ubicacionVoto);
        voto.setProcesoElectoral(uVotoCreateDTO.getProcesoElectoral());
        voto.setCandidatura(uVotoCreateDTO.getCandidatura());

        // Guardar voto
        votoRepository.save(voto);

        // Actualizar estado del usuario si aplica
        usuario.setEstado(true);
        usuarioRepository.save(usuario);

        return new UVotoDTO(voto);
    }

}
