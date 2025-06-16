package com.socies.voto;

import com.socies.voto.dtos.Voto.AVotoDTO;
import com.socies.voto.dtos.Voto.UVotoCreateDTO;
import com.socies.voto.dtos.Voto.UVotoDTO;
import com.socies.voto.dtos.usuario.UsuarioPrincipalDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.*;
import com.socies.voto.repositories.MetodoVotoRepository;
import com.socies.voto.repositories.UbicacionVotoRepository;
import com.socies.voto.repositories.UsuarioRepository;
import com.socies.voto.repositories.VotoRepository;
import com.socies.voto.services.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VotoServiceTests {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MetodoVotoRepository metodoVotoRepository;

    @Mock
    private UbicacionVotoRepository ubicacionVotoRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private VotoService votoService;

    private Usuario usuario;
    private ProcesoElectoral procesoElectoral;
    private MetodoVoto metodoVoto;
    private UbicacionVoto ubicacionVoto;
    private Candidatura candidatura;
    private UVotoCreateDTO votoDTO;
    private UsuarioPrincipalDTO usuarioPrincipal;

    private EstadoCandidato estadoCandidato;
    private Cargo cargo;
    private Partido partido;
    private EstadoCandidatura estadoCandidatura;

    private Municipio municipio;
    private Provincia provincia;
    private Departamento departamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEstado(false);

        procesoElectoral = new ProcesoElectoral();
        procesoElectoral.setId(1L);

        metodoVoto = new MetodoVoto();
        metodoVoto.setId(1L);

        departamento = new Departamento();
        departamento.setId(1L);

        provincia = new Provincia();
        provincia.setId(1L);
        provincia.setDepartamento(departamento);

        municipio = new Municipio();
        municipio.setId(1L);
        municipio.setProvincia(provincia);

        ubicacionVoto = new UbicacionVoto();
        ubicacionVoto.setId(1L);
        ubicacionVoto.setMunicipio(municipio);

        // Asegurarse de que la candidatura tenga candidato
        EstadoCandidato estadoCandidato = new EstadoCandidato();
        estadoCandidato.setId(1L);

        Cargo cargo = new Cargo();
        cargo.setId(1L);

        Candidato candidato = new Candidato();
        candidato.setId(1L);
        candidato.setEstadoCandidato(estadoCandidato);
        candidato.setCargo(cargo);


        partido = new Partido();
        partido.setId(1L);

        estadoCandidatura = new EstadoCandidatura();
        estadoCandidatura.setId(1L);

        candidatura = new Candidatura();
        candidatura.setId(1L);
        candidatura.setCandidato(candidato); // ¡esto es clave!
        candidatura.setPartido(partido);
        candidatura.setEstadoCandidatura(estadoCandidatura);
        candidatura.setProcesoElectoral(procesoElectoral);

        votoDTO = new UVotoCreateDTO();
        votoDTO.setProcesoElectoral(procesoElectoral);
        votoDTO.setMetodoVoto(metodoVoto);
        votoDTO.setUbicacionVoto(ubicacionVoto);
        votoDTO.setCandidatura(candidatura);

        usuarioPrincipal = mock(UsuarioPrincipalDTO.class);
        when(usuarioPrincipal.getId()).thenReturn(1L);
        when(usuarioPrincipal.puedeVotar()).thenReturn(true);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuarioPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void testVotoExitoso() {
        when(votoRepository.existsByUsuarioIdAndProcesoElectoralId(1L, 1L)).thenReturn(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(metodoVotoRepository.findById(1L)).thenReturn(Optional.of(metodoVoto));
        when(ubicacionVotoRepository.findById(1L)).thenReturn(Optional.of(ubicacionVoto));

        UVotoDTO resultado = votoService.save(votoDTO);

        assertNotNull(resultado);
        verify(votoRepository).save(any(Voto.class));
        verify(usuarioRepository).save(usuario);
        assertTrue(usuario.isEstado());
    }

    @Test
    void testUsuarioYaVoto() {
        when(votoRepository.existsByUsuarioIdAndProcesoElectoralId(1L, 1L)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            votoService.save(votoDTO);
        });

        assertEquals("El usuario ya ha emitido su voto en este proceso electoral.", ex.getMessage());
        verify(votoRepository, never()).save(any());
    }

    @Test
    void testUsuarioSinPermisoParaVotar() {
        when(usuarioPrincipal.puedeVotar()).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            votoService.save(votoDTO);
        });

        assertEquals("El usuario no puede votar", ex.getMessage());
        verify(votoRepository, never()).save(any());
    }
}
