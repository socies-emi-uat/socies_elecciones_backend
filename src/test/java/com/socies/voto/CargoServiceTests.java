package com.socies.voto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.socies.voto.dtos.Cargo.CargoCreateDTO;
import com.socies.voto.dtos.Cargo.CargoDTO;
import com.socies.voto.exceptions.Cargo.CargoAlreadyExistsException;
import com.socies.voto.exceptions.Cargo.CargoNotFoundException;
import com.socies.voto.models.Candidato;
import com.socies.voto.models.Cargo;
import com.socies.voto.repositories.CargoRepository;
import com.socies.voto.services.CargoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CargoServiceTests {

    @InjectMocks private CargoService cargoService;

    @Mock private CargoRepository cargoRepository;

    @Test
    void testObtenerTodosLosCargos() {
        Cargo cargo1 = new Cargo("Presidente", "Dirige la organización");
        cargo1.setId(1L);
        Cargo cargo2 = new Cargo("Tesorero", "Administra fondos");
        cargo2.setId(2L);

        when(cargoRepository.findAll()).thenReturn(List.of(cargo1, cargo2));

        List<CargoDTO> resultado = cargoService.obtenerTodosLosCargos();

        assertEquals(2, resultado.size());
        assertEquals("Presidente", resultado.get(0).getNombre_cargo());
        assertEquals("Tesorero", resultado.get(1).getNombre_cargo());
    }

    @Test
    void testObtenerCargoPorIdExiste() {
        Cargo cargo = new Cargo("Secretario", "Redacta actas");
        cargo.setId(5L);

        when(cargoRepository.findById(5L)).thenReturn(Optional.of(cargo));

        CargoDTO dto = cargoService.obtenerCargoPorId(5L);

        assertEquals("Secretario", dto.getNombre_cargo());
        assertEquals("Redacta actas", dto.getDescripcion());
    }

    @Test
    void testObtenerCargoPorIdNoExiste() {
        when(cargoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CargoNotFoundException.class, () -> cargoService.obtenerCargoPorId(999L));
    }

    @Test
    void testCrearCargoExitosamente() {
        CargoCreateDTO dto = new CargoCreateDTO(1L, "Vocal", "Apoya decisiones");

        when(cargoRepository.findByNombre("Vocal")).thenReturn(Optional.empty());

        Cargo cargoGuardado = new Cargo("Vocal", "Apoya decisiones");
        cargoGuardado.setId(7L);
        when(cargoRepository.save(any())).thenReturn(cargoGuardado);

        CargoDTO result = cargoService.CrearCargo(dto);

        assertNotNull(result);
        assertEquals("Vocal", result.getNombre_cargo());
        assertEquals("Apoya decisiones", result.getDescripcion());
    }

    @Test
    void testCrearCargoYaExiste() {
        Cargo existente = new Cargo("Presidente", "Ya registrado");
        when(cargoRepository.findByNombre("Presidente")).thenReturn(Optional.of(existente));

        CargoCreateDTO dto = new CargoCreateDTO(1L, "Presidente", "Intento duplicado");

        assertThrows(CargoAlreadyExistsException.class, () -> cargoService.CrearCargo(dto));
    }

    @Test
    void testEliminarCargoExitoso() {
        Cargo cargo = new Cargo("Vocal", "Sin candidatos");
        cargo.setId(10L);
        cargo.setCandidatos(new ArrayList<>());

        when(cargoRepository.findById(10L)).thenReturn(Optional.of(cargo));

        cargoService.eliminarCargo(10L);

        verify(cargoRepository, times(1)).delete(cargo);
    }

    @Test
    void testEliminarCargoConCandidatos() {
        Cargo cargo = new Cargo("Presidente", "Con candidatos");
        cargo.setId(11L);

        // ✅ Simular un candidato asociado
        Candidato candidatoMock = mock(Candidato.class);
        cargo.setCandidatos(List.of(candidatoMock));

        when(cargoRepository.findById(11L)).thenReturn(Optional.of(cargo));

        assertThrows(IllegalStateException.class, () -> cargoService.eliminarCargo(11L));
        verify(cargoRepository, never()).delete(any());
    }

    @Test
    void testEliminarCargoNoExiste() {
        when(cargoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CargoNotFoundException.class, () -> cargoService.eliminarCargo(999L));
    }

    @Test
    void testActualizarCargoExitoso() {
        Cargo cargoExistente = new Cargo("Antiguo", "Antiguo desc");
        cargoExistente.setId(20L);

        CargoCreateDTO dto = new CargoCreateDTO(20L, "Nuevo", "Nueva desc");

        when(cargoRepository.findById(20L)).thenReturn(Optional.of(cargoExistente));
        when(cargoRepository.findByNombre("Nuevo")).thenReturn(Optional.empty());

        when(cargoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CargoDTO actualizado = cargoService.actualizarCargo(20L, dto);

        assertEquals("Nuevo", actualizado.getNombre_cargo());
        assertEquals("Nueva desc", actualizado.getDescripcion());
    }

    @Test
    void testActualizarCargoConNombreExistenteEnOtro() {
        Cargo original = new Cargo("Original", "Descripción");
        original.setId(30L);
        Cargo duplicado = new Cargo("Duplicado", "Otra");
        duplicado.setId(31L);

        CargoCreateDTO dto = new CargoCreateDTO(30L, "Duplicado", "Actualizado");

        when(cargoRepository.findById(30L)).thenReturn(Optional.of(original));
        when(cargoRepository.findByNombre("Duplicado")).thenReturn(Optional.of(duplicado));

        assertThrows(
                CargoAlreadyExistsException.class, () -> cargoService.actualizarCargo(30L, dto));
    }

    @Test
    void testBuscarCargosFiltradosConNombre() {
        Cargo cargo = new Cargo("Secretario", "Organiza actas");

        when(cargoRepository.findByNombreContainingIgnoreCase("secret")).thenReturn(List.of(cargo));

        List<CargoDTO> resultado = cargoService.buscarCargosFiltrados("secret", null);

        assertEquals(1, resultado.size());
        assertEquals("Secretario", resultado.get(0).getNombre_cargo());
    }

    @Test
    void testBuscarCargosFiltradosConDescripcion() {
        Cargo cargo = new Cargo("Asistente", "Soporte técnico");

        when(cargoRepository.findByDescripcionContainingIgnoreCase("soporte"))
                .thenReturn(List.of(cargo));

        List<CargoDTO> resultado = cargoService.buscarCargosFiltrados(null, "soporte");

        assertEquals(1, resultado.size());
        assertEquals("Asistente", resultado.get(0).getNombre_cargo());
    }

    @Test
    void testBuscarCargosFiltradosConAmbos() {
        Cargo cargo = new Cargo("Asistente", "Soporte técnico");

        when(cargoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
                        "asistente", "técnico"))
                .thenReturn(List.of(cargo));

        List<CargoDTO> resultado = cargoService.buscarCargosFiltrados("asistente", "técnico");

        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarCargosFiltradosSinFiltros() {
        List<Cargo> cargos =
                List.of(new Cargo("Cargo 1", "Desc 1"), new Cargo("Cargo 2", "Desc 2"));
        when(cargoRepository.findAll()).thenReturn(cargos);

        List<CargoDTO> resultado = cargoService.buscarCargosFiltrados(null, null);

        assertEquals(2, resultado.size());
    }
}
