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
        assertEquals("Activo", resultado.get(0).getEstado_proceso());
        assertEquals("Finalizado", resultado.get(1).getEstado_proceso());

        Mockito.verify(estadoProcesoRepository).findAll();
    }

    @Test
    void testObtenerEstadoProcesoPorIdExiste() {
        EstadoProceso ep = new EstadoProceso("En espera");
        ep.setId(1L);

        Mockito.when(estadoProcesoRepository.findById(1L)).thenReturn(Optional.of(ep));

        EstadoProcesoDTO dto = estadoProcesoService.obtenerEstadoProcesoPorId(1L);

        assertEquals("En espera", dto.getEstado_proceso());
        assertEquals(1L, dto.getId());

        Mockito.verify(estadoProcesoRepository).findById(1L);
    }

    @Test
    void testObtenerEstadoProcesoPorIdNoExiste() {
        Mockito.when(estadoProcesoRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            estadoProcesoService.obtenerEstadoProcesoPorId(999L);
        });

        assertEquals("El estado de proceso no fue encontrado", exception.getMessage());

        Mockito.verify(estadoProcesoRepository).findById(999L);
    }

    @Test
    void testCrearEstadoProceso() {
        EstadoProcesoCreateDTO dto = new EstadoProcesoCreateDTO("Nuevo Estado");

        Mockito.when(estadoProcesoRepository.findByEstadoProceso("Nuevo Estado")).thenReturn(Optional.empty());

        EstadoProceso ep = new EstadoProceso("Nuevo Estado");
        ep.setId(1L);

        Mockito.when(estadoProcesoRepository.save(Mockito.any(EstadoProceso.class))).thenReturn(ep);

        EstadoProcesoDTO result = estadoProcesoService.crearEstadoProceso(dto);

        assertEquals("Nuevo Estado", result.getEstado_proceso());
        assertEquals(1L, result.getId());

        Mockito.verify(estadoProcesoRepository).findByEstadoProceso("Nuevo Estado");
        Mockito.verify(estadoProcesoRepository).save(Mockito.any(EstadoProceso.class));
    }

    @Test
    void testCrearEstadoProcesoYaExiste() {
        EstadoProceso existente = new EstadoProceso("Estado Existente");

        Mockito.when(estadoProcesoRepository.findByEstadoProceso("Estado Existente")).thenReturn(Optional.of(existente));

        EstadoProcesoCreateDTO dto = new EstadoProcesoCreateDTO("Estado Existente");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            estadoProcesoService.crearEstadoProceso(dto);
        });

        assertEquals("El estado de proceso ya existe", exception.getMessage());

        Mockito.verify(estadoProcesoRepository).findByEstadoProceso("Estado Existente");
    }
}
