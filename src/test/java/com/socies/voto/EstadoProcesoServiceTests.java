package com.socies.voto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.socies.voto.dtos.EstadoProceso.EstadoProcesoCreateDTO;
import com.socies.voto.dtos.EstadoProceso.EstadoProcesoDTO;
import com.socies.voto.dtos.EstadoProceso.EstadoProcesoUpdateDTO;
import com.socies.voto.exceptions.EstadoProceso.EstadoProcesoAlreadyExistsException;
import com.socies.voto.exceptions.EstadoProceso.EstadoProcesoNotFoundException;
import com.socies.voto.models.EstadoProceso;
import com.socies.voto.repositories.EstadoProcesoRepository;
import com.socies.voto.services.EstadoProcesoService;

@ExtendWith(MockitoExtension.class)
public class EstadoProcesoServiceTests {

    @InjectMocks
    private EstadoProcesoService estadoProcesoService;

    @Mock
    private EstadoProcesoRepository estadoProcesoRepository;

    @Test
    void testObtenerTodosLosEstadosProceso() {
        EstadoProceso ep1 = new EstadoProceso("Activo");
        ep1.setId(1L);
        EstadoProceso ep2 = new EstadoProceso("Finalizado");
        ep2.setId(2L);

        Mockito.when(estadoProcesoRepository.findAll()).thenReturn(List.of(ep1, ep2));

        List<EstadoProcesoDTO> resultado = estadoProcesoService.obtenerTodosLosEstadosProceso();

        assertEquals(2, resultado.size());
        assertEquals("Activo", resultado.get(0).getEstadoProceso());
        assertEquals("Finalizado", resultado.get(1).getEstadoProceso());

        Mockito.verify(estadoProcesoRepository).findAll();
    }

    @Test
    void testObtenerEstadoProcesoPorIdExiste() {
        EstadoProceso ep = new EstadoProceso("En espera");
        ep.setId(1L);

        Mockito.when(estadoProcesoRepository.findById(1L)).thenReturn(Optional.of(ep));

        EstadoProcesoDTO dto = estadoProcesoService.obtenerEstadoProcesoPorId(1L);

        assertEquals("En espera", dto.getEstadoProceso());
        assertEquals(1L, dto.getId());

        Mockito.verify(estadoProcesoRepository).findById(1L);
    }

    @Test
    void testObtenerEstadoProcesoPorIdNoExiste() {
        Mockito.when(estadoProcesoRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EstadoProcesoNotFoundException.class, () -> {
            estadoProcesoService.obtenerEstadoProcesoPorId(999L);
        });

        assertEquals("El estado de proceso no fue encontrado", exception.getMessage());
        Mockito.verify(estadoProcesoRepository).findById(999L);
    }

    @Test
    void testCrearEstadoProcesoYaExiste() {
        EstadoProceso existente = new EstadoProceso("Estado Existente");

        Mockito.when(estadoProcesoRepository.findByEstadoProceso("Estado Existente"))
               .thenReturn(Optional.of(existente));

        EstadoProcesoCreateDTO dto = new EstadoProcesoCreateDTO("Estado Existente");

        Exception exception = assertThrows(EstadoProcesoAlreadyExistsException.class, () -> {
            estadoProcesoService.crearEstadoProceso(dto);
        });

        assertEquals("El estado de proceso ya existe", exception.getMessage());
        Mockito.verify(estadoProcesoRepository).findByEstadoProceso("Estado Existente");
    }
    @Test
        void testActualizarEstadoProcesoExitoso() {
        // Configuración inicial
        Long id = 1L;
        EstadoProcesoUpdateDTO updateDTO = new EstadoProcesoUpdateDTO();
        updateDTO.setEstadoProceso("Nuevo Nombre");
        
        EstadoProceso estadoExistente = new EstadoProceso("Nombre Antiguo");
        estadoExistente.setId(id);
        
        EstadoProceso estadoActualizado = new EstadoProceso("Nuevo Nombre");
        estadoActualizado.setId(id);

        // Mock del repositorio
        Mockito.when(estadoProcesoRepository.findById(id))
            .thenReturn(Optional.of(estadoExistente));
        
        Mockito.when(estadoProcesoRepository.findByEstadoProceso("Nuevo Nombre"))
            .thenReturn(Optional.empty());
        
        Mockito.when(estadoProcesoRepository.save(Mockito.any(EstadoProceso.class)))
            .thenReturn(estadoActualizado);

        // Ejecución
        EstadoProcesoDTO resultado = estadoProcesoService.actualizarEstadoProceso(id, updateDTO);

        // Verificaciones
        assertEquals("Nuevo Nombre", resultado.getEstadoProceso());
        assertEquals(id, resultado.getId());
        
        Mockito.verify(estadoProcesoRepository).findById(id);
        Mockito.verify(estadoProcesoRepository).findByEstadoProceso("Nuevo Nombre");
        Mockito.verify(estadoProcesoRepository).save(Mockito.any(EstadoProceso.class));
    }

    @Test
    void testActualizarEstadoProcesoNoExiste() {
        Long id = 999L;
        EstadoProcesoUpdateDTO updateDTO = new EstadoProcesoUpdateDTO();
        updateDTO.setEstadoProceso("Nuevo Nombre");

        Mockito.when(estadoProcesoRepository.findById(id))
            .thenReturn(Optional.empty());

        // Ejecución y verificación
        Exception exception = assertThrows(EstadoProcesoNotFoundException.class, () -> {
            estadoProcesoService.actualizarEstadoProceso(id, updateDTO);
        });

        assertEquals("Estado de proceso no encontrado", exception.getMessage());
        Mockito.verify(estadoProcesoRepository).findById(id);
        Mockito.verify(estadoProcesoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testActualizarEstadoProcesoNombreDuplicado() {
        Long id = 1L;
        EstadoProcesoUpdateDTO updateDTO = new EstadoProcesoUpdateDTO();
        updateDTO.setEstadoProceso("Nombre Existente");
        
        EstadoProceso estadoExistente = new EstadoProceso("Nombre Antiguo");
        estadoExistente.setId(id);
        
        EstadoProceso otroEstado = new EstadoProceso("Nombre Existente");
        otroEstado.setId(2L);

        // Mock del repositorio
        Mockito.when(estadoProcesoRepository.findById(id))
            .thenReturn(Optional.of(estadoExistente));
        
        Mockito.when(estadoProcesoRepository.findByEstadoProceso("Nombre Existente"))
            .thenReturn(Optional.of(otroEstado));

        // Ejecución y verificación
        Exception exception = assertThrows(EstadoProcesoAlreadyExistsException.class, () -> {
            estadoProcesoService.actualizarEstadoProceso(id, updateDTO);
        });

        assertEquals("El nombre del estado ya está en uso", exception.getMessage());
        Mockito.verify(estadoProcesoRepository).findById(id);
        Mockito.verify(estadoProcesoRepository).findByEstadoProceso("Nombre Existente");
        Mockito.verify(estadoProcesoRepository, Mockito.never()).save(Mockito.any());
    }
}