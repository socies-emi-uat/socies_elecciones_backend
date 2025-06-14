package com.socies.voto;

import com.socies.voto.dtos.Partido.PartidoCreateDTO;
import com.socies.voto.dtos.Partido.PartidoUpdateDTO;
import com.socies.voto.exceptions.ResourceAlreadyExistsException;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Partido;
import com.socies.voto.repositories.PartidoRepository;
import com.socies.voto.services.PartidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartidoServiceTests {

    @Mock
    private PartidoRepository partidoRepository;

    @InjectMocks
    private PartidoService partidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePartido_Success() {
        PartidoCreateDTO dto = new PartidoCreateDTO();
        dto.setNombrePartido("Nuevo Partido");
        dto.setSigla("NP");

        when(partidoRepository.findByNombrePartido("Nuevo Partido")).thenReturn(Optional.empty());
        when(partidoRepository.findBySigla("NP")).thenReturn(Optional.empty());

        Partido savedPartido = new Partido();
        savedPartido.setNombrePartido("Nuevo Partido");
        savedPartido.setSigla("NP");

        when(partidoRepository.save(any(Partido.class))).thenReturn(savedPartido);

        var result = partidoService.create(dto);
        assertEquals("Nuevo Partido", result.getNombrePartido());
        assertEquals("NP", result.getSigla());
    }

    @Test
    void testCreatePartido_DuplicateNombre() {
        PartidoCreateDTO dto = new PartidoCreateDTO();
        dto.setNombrePartido("Existente");
        dto.setSigla("SIGLA");

        when(partidoRepository.findByNombrePartido("Existente"))
                .thenReturn(Optional.of(new Partido()));

        assertThrows(ResourceAlreadyExistsException.class, () -> partidoService.create(dto));
    }

    @Test
    void testUpdatePartido_Success() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.setNombrePartido("Viejo");
        partido.setSigla("VP");

        PartidoUpdateDTO dto = new PartidoUpdateDTO();
        dto.setNombrePartido("Nuevo");
        dto.setSigla("NP");

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(partidoRepository.findByNombrePartido("Nuevo")).thenReturn(Optional.empty());
        when(partidoRepository.findBySigla("NP")).thenReturn(Optional.empty());

        when(partidoRepository.save(any(Partido.class))).thenReturn(partido);

        var result = partidoService.update(1L, dto);
        assertEquals("Nuevo", result.getNombrePartido());
        assertEquals("NP", result.getSigla());
    }

    @Test
    void testUpdatePartido_NotFound() {
        when(partidoRepository.findById(999L)).thenReturn(Optional.empty());
        PartidoUpdateDTO dto = new PartidoUpdateDTO();
        dto.setNombrePartido("Nuevo");
        dto.setSigla("NP");

        assertThrows(ResourceNotFoundException.class, () -> partidoService.update(999L, dto));
    }

    @Test
    void testDisableOenable() {
        Partido partido = new Partido();
        partido.setId(1L);
        partido.setEstado(true);

        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));

        String mensaje = partidoService.disableOenable(1L);

        assertFalse(partido.isEstado());
        assertEquals("El partido fue desabilitado.", mensaje);
    }

    @Test
    void testDisableOenable_NotFound() {
        when(partidoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> partidoService.disableOenable(2L));
    }
}
