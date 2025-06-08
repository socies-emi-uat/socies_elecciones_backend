package com.socies.voto;

import static org.junit.jupiter.api.Assertions.*;

import com.socies.voto.dtos.Cargo.CargoCreateDTO;
import com.socies.voto.dtos.Cargo.CargoDTO;
import com.socies.voto.exceptions.Cargo.CargoAlreadyExistsException;
import com.socies.voto.exceptions.Cargo.CargoNotFoundException;
import com.socies.voto.models.Cargo;
import com.socies.voto.repositories.CargoRepository;
import com.socies.voto.services.CargoService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CargoServiceTests {

    @InjectMocks private CargoService cargoService;

    @Mock private CargoRepository cargoRepository;

    @Test
    void testObtenerTodosLosCargos() {
        // Mock de datos
        Cargo cargo1 = new Cargo("Presidente", "Dirige la organización");
        cargo1.setId(1L);
        Cargo cargo2 = new Cargo("Tesorero", "Administra fondos");
        cargo2.setId(2L);

        List<Cargo> cargos = List.of(cargo1, cargo2);
        Mockito.when(cargoRepository.findAll()).thenReturn(cargos);

        // Ejecutar
        List<CargoDTO> resultado = cargoService.obtenerTodosLosCargos();

        // Verificar
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Presidente", resultado.get(0).getNombre_cargo());
        assertEquals("Tesorero", resultado.get(1).getNombre_cargo());
    }

    @Test
    void testObtenerCargoPorIdExiste() {
        Cargo cargo = new Cargo("Secretario", "Redacta actas");
        cargo.setId(5L);

        Mockito.when(cargoRepository.findById(5L)).thenReturn(Optional.of(cargo));

        CargoDTO dto = cargoService.obtenerCargoPorId(5L);

        assertNotNull(dto);
        assertEquals("Secretario", dto.getNombre_cargo());
        assertEquals("Redacta actas", dto.getDescripcion());
    }

    @Test
    void testObtenerCargoPorIdNoExiste() {
        Mockito.when(cargoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CargoNotFoundException.class, () -> cargoService.obtenerCargoPorId(999L));
    }

    @Test
    void testCrearCargoExitosamente() {
        CargoCreateDTO dto = new CargoCreateDTO(1L, "Vocal", "Apoya decisiones");

        // El nombre no existe aún
        Mockito.when(cargoRepository.findByNombre("Vocal")).thenReturn(Optional.empty());

        Cargo cargoGuardado = new Cargo("Vocal", "Apoya decisiones");
        cargoGuardado.setId(7L);
        Mockito.when(cargoRepository.save(Mockito.any(Cargo.class))).thenReturn(cargoGuardado);

        CargoDTO result = cargoService.CrearCargo(dto);

        assertNotNull(result);
        assertEquals("Vocal", result.getNombre_cargo());
        assertEquals("Apoya decisiones", result.getDescripcion());
    }

    @Test
    void testCrearCargoYaExiste() {
        Cargo existente = new Cargo("Presidente", "Ya registrado");
        Mockito.when(cargoRepository.findByNombre("Presidente")).thenReturn(Optional.of(existente));

        CargoCreateDTO dto = new CargoCreateDTO(1L, "Presidente", "Intento duplicado");

        assertThrows(CargoAlreadyExistsException.class, () -> cargoService.CrearCargo(dto));
    }

    @Test
    void testEliminarCargo() {
        Long id = 10L;

        // No lanza excepción si el ID existe o no
        Mockito.doNothing().when(cargoRepository).deleteById(id);

        cargoService.eliminarCargo(id);

        Mockito.verify(cargoRepository, Mockito.times(1)).deleteById(id);
    }
}
