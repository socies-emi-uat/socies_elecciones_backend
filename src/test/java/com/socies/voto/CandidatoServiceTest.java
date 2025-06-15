package com.socies.voto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.socies.voto.dtos.Candidato.CandidatoCreateDTO;
import com.socies.voto.dtos.Candidato.CandidatoDTO;
import com.socies.voto.dtos.Candidato.CandidatoUpdateDTO;
import com.socies.voto.exceptions.Candidato.CandidatoAlreadyExistsException;
import com.socies.voto.exceptions.Candidato.CandidatoNotFoundException;
import com.socies.voto.models.Candidato;
import com.socies.voto.models.Cargo;
import com.socies.voto.models.EstadoCandidato;
import com.socies.voto.repositories.CandidatoRepository;
import com.socies.voto.repositories.CargoRepository;
import com.socies.voto.repositories.EstadoCandidatoRepository;
import com.socies.voto.services.CandidatoService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CandidatoServiceTest {

    @Mock private CandidatoRepository candidatoRepository;

    @Mock private EstadoCandidatoRepository estadoCandidatoRepository;

    @Mock private CargoRepository cargoRepository;

    @InjectMocks private CandidatoService candidatoService;

    private Candidato candidato;
    private CandidatoCreateDTO createDTO;
    private CandidatoUpdateDTO updateDTO;
    private EstadoCandidato estadoCandidato;
    private Cargo cargo;

    @BeforeEach
    void setUp() {
        // Configuración común para todas las pruebas
        estadoCandidato = new EstadoCandidato();
        estadoCandidato.setId(1L);

        cargo = new Cargo();
        cargo.setId(1L);

        candidato = new Candidato();
        candidato.setId(1L);
        candidato.setNombreCandidato("Juan");
        candidato.setApellidoPaterno("Perez");
        candidato.setApellidoMaterno("Gomez");
        candidato.setCedulaIdentidad("12345678");
        candidato.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        candidato.setFotoUrl("foto.jpg");
        candidato.setCorreo("juan@test.com");
        candidato.setPropuesta("Propuesta de prueba");
        candidato.setEstadoCandidato(estadoCandidato);
        candidato.setCargo(cargo);

        createDTO = new CandidatoCreateDTO();
        createDTO.setNombreCandidato("Maria");
        createDTO.setApPaterno("Gomez");
        createDTO.setApMaterno("Lopez");
        createDTO.setCiCandidato("87654321");
        createDTO.setFechaNacimiento(LocalDate.of(1995, 5, 15));
        createDTO.setFotoUrl("foto-maria.jpg");
        createDTO.setCorreoCandidato("maria@test.com");
        createDTO.setPropuesta("Nueva propuesta");
        createDTO.setEstadoCandidatoId(1L);
        createDTO.setCargoId(1L);

        updateDTO = new CandidatoUpdateDTO();
        updateDTO.setNombreCandidato("Juan Actualizado");
        updateDTO.setApPaterno("Perez Actualizado");
        updateDTO.setApMaterno("Gomez Actualizado");
        updateDTO.setCiCandidato("12345678");
        updateDTO.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        updateDTO.setFotoUrl("foto-actualizada.jpg");
        updateDTO.setCorreoCandidato("juan-actualizado@test.com");
        updateDTO.setPropuesta("Propuesta actualizada");
        updateDTO.setEstadoCandidatoId(1L);
        updateDTO.setCargoId(1L);
    }

    @Test
    void obtenerTodosLosCandidatos_RetornaListaDeCandidatos() {
        when(candidatoRepository.findAll()).thenReturn(List.of(candidato));

        List<CandidatoDTO> resultado = candidatoService.obtenerTodosLosCandidatos();

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombreCandidato());
        verify(candidatoRepository).findAll();
    }

    @Test
    void obtenerCandidatoPorId_CandidatoExiste_RetornaCandidatoDTO() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));

        CandidatoDTO resultado = candidatoService.obtenerCandidatoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombreCandidato());
        verify(candidatoRepository).findById(1L);
    }

    @Test
    void obtenerCandidatoPorId_CandidatoNoExiste_LanzaExcepcion() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                CandidatoNotFoundException.class,
                () -> {
                    candidatoService.obtenerCandidatoPorId(1L);
                });
        verify(candidatoRepository).findById(1L);
    }

    @Test
    void crearCandidato_DatosValidos_RetornaCandidatoCreado() {
        when(candidatoRepository.findByCedulaIdentidad("87654321")).thenReturn(Optional.empty());
        when(candidatoRepository.findByCorreo("maria@test.com")).thenReturn(Optional.empty());
        when(estadoCandidatoRepository.findById(1L)).thenReturn(Optional.of(estadoCandidato));
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        CandidatoDTO resultado = candidatoService.crearCandidato(createDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(candidatoRepository).save(any(Candidato.class));
    }

    @Test
    void crearCandidato_CIDuplicado_LanzaExcepcion() {
        when(candidatoRepository.findByCedulaIdentidad("87654321"))
                .thenReturn(Optional.of(new Candidato()));

        assertThrows(
                CandidatoAlreadyExistsException.class,
                () -> {
                    candidatoService.crearCandidato(createDTO);
                });
        verify(candidatoRepository, never()).save(any());
    }

    @Test
    void crearCandidato_CorreoDuplicado_LanzaExcepcion() {
        when(candidatoRepository.findByCedulaIdentidad("87654321")).thenReturn(Optional.empty());
        when(candidatoRepository.findByCorreo("maria@test.com"))
                .thenReturn(Optional.of(new Candidato()));

        assertThrows(
                CandidatoAlreadyExistsException.class,
                () -> {
                    candidatoService.crearCandidato(createDTO);
                });
        verify(candidatoRepository, never()).save(any());
    }

    @Test
    void crearCandidato_PropuestaVacia_LanzaExcepcion() {
        createDTO.setPropuesta("");

        assertThrows(
                RuntimeException.class,
                () -> {
                    candidatoService.crearCandidato(createDTO);
                },
                "La propuesta es requerida");
        verify(candidatoRepository, never()).save(any());
    }

    @Test
    void actualizarCandidato_DatosValidos_RetornaCandidatoActualizado() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(candidatoRepository.findByCedulaIdentidad("12345678"))
                .thenReturn(Optional.of(candidato));
        when(candidatoRepository.findByCorreo("juan-actualizado@test.com"))
                .thenReturn(Optional.empty());
        when(estadoCandidatoRepository.findById(1L)).thenReturn(Optional.of(estadoCandidato));
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        CandidatoDTO resultado = candidatoService.actualizarCandidato(1L, updateDTO);

        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombreCandidato());
        verify(candidatoRepository).save(any(Candidato.class));
    }

    @Test
    void actualizarCandidato_CandidatoNoExiste_LanzaExcepcion() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                CandidatoNotFoundException.class,
                () -> {
                    candidatoService.actualizarCandidato(1L, updateDTO);
                });
        verify(candidatoRepository, never()).save(any());
    }

    @Test
    void actualizarCandidato_CIExistenteEnOtroRegistro_LanzaExcepcion() {
        Candidato otroCandidato = new Candidato();
        otroCandidato.setId(2L);
        otroCandidato.setCedulaIdentidad("12345678");

        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(candidatoRepository.findByCedulaIdentidad("12345678"))
                .thenReturn(Optional.of(otroCandidato));

        assertThrows(
                CandidatoAlreadyExistsException.class,
                () -> {
                    candidatoService.actualizarCandidato(1L, updateDTO);
                });
        verify(candidatoRepository, never()).save(any());
    }

    @Test
    void actualizarCandidato_PropuestaVacia_LanzaExcepcion() {
        updateDTO.setPropuesta("");

        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));

        assertThrows(
                RuntimeException.class,
                () -> {
                    candidatoService.actualizarCandidato(1L, updateDTO);
                },
                "La propuesta es requerida");
        verify(candidatoRepository, never()).save(any());
    }

    @Test
    void eliminarCandidato_IdValido_EliminaCorrectamente() {
        when(candidatoRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(
                () -> {
                    candidatoService.eliminarCandidato(1L);
                });
        verify(candidatoRepository).deleteById(1L);
    }

    @Test
    void eliminarCandidato_CandidatoNoExiste_LanzaExcepcion() {
        when(candidatoRepository.existsById(1L)).thenReturn(false);

        assertThrows(
                CandidatoNotFoundException.class,
                () -> {
                    candidatoService.eliminarCandidato(1L);
                });
        verify(candidatoRepository, never()).deleteById(any());
    }
}
