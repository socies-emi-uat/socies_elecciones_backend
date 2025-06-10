package com.socies.voto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoCreateDTO;
import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoDTO;
import com.socies.voto.exceptions.EstadoCandidato.EstadoCandidatoAlreadyExistsException;
import com.socies.voto.exceptions.EstadoCandidato.EstadoCandidatoNotFoundException;
import com.socies.voto.models.EstadoCandidato;
import com.socies.voto.repositories.EstadoCandidatoRepository;
import com.socies.voto.services.EstadoCandidatoServices;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EstadoCandidatoServiceTests {

    @Mock private EstadoCandidatoRepository estadoCandidatoRepository;

    @InjectMocks private EstadoCandidatoServices estadoCandidatoService;

    @Test
    void testGetAllEstadoCandidatos() {
        // Arrange
        EstadoCandidato estado1 = new EstadoCandidato("Activo");
        estado1.setId(1L);
        EstadoCandidato estado2 = new EstadoCandidato("Inactivo");
        estado2.setId(2L);

        when(estadoCandidatoRepository.findAll()).thenReturn(Arrays.asList(estado1, estado2));

        // Act
        List<EstadoCandidatoDTO> result = estadoCandidatoService.getAllEstadoCandidatos();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Activo", result.get(0).getEstado_candidato());
        assertEquals("Inactivo", result.get(1).getEstado_candidato());
        verify(estadoCandidatoRepository).findAll();
    }

    @Test
    void testGetEstadoCandidatoById_Success() {
        // Arrange
        EstadoCandidato estado = new EstadoCandidato("Activo");
        estado.setId(1L);

        when(estadoCandidatoRepository.findById(1L)).thenReturn(Optional.of(estado));

        // Act
        EstadoCandidatoDTO result = estadoCandidatoService.getEstadoCandidatoById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Activo", result.getEstado_candidato());
        verify(estadoCandidatoRepository).findById(1L);
    }

    @Test
    void testGetEstadoCandidatoById_NotFound() {
        // Arrange
        when(estadoCandidatoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                EstadoCandidatoNotFoundException.class,
                () -> {
                    estadoCandidatoService.getEstadoCandidatoById(999L);
                });
        verify(estadoCandidatoRepository).findById(999L);
    }

    @Test
    void testCreateEstadoCandidato_Success() {
        // Arrange
        EstadoCandidatoCreateDTO createDTO = new EstadoCandidatoCreateDTO(null);
        createDTO.setEstado_candidato("Nuevo Estado");

        EstadoCandidato savedEstado = new EstadoCandidato("Nuevo Estado");
        savedEstado.setId(1L);

        when(estadoCandidatoRepository.findByEstadoCandidato("Nuevo Estado"))
                .thenReturn(Optional.empty());
        when(estadoCandidatoRepository.save(any(EstadoCandidato.class))).thenReturn(savedEstado);

        // Act
        EstadoCandidatoDTO result = estadoCandidatoService.createEstadoCandidato(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nuevo Estado", result.getEstado_candidato());
        verify(estadoCandidatoRepository).findByEstadoCandidato("Nuevo Estado");
        verify(estadoCandidatoRepository).save(any(EstadoCandidato.class));
    }

    @Test
    void testCreateEstadoCandidato_AlreadyExists() {
        // Arrange
        EstadoCandidatoCreateDTO createDTO = new EstadoCandidatoCreateDTO(null);
        createDTO.setEstado_candidato("Existente");

        EstadoCandidato existing = new EstadoCandidato("Existente");
        when(estadoCandidatoRepository.findByEstadoCandidato("Existente"))
                .thenReturn(Optional.of(existing));

        // Act & Assert
        assertThrows(
                EstadoCandidatoAlreadyExistsException.class,
                () -> {
                    estadoCandidatoService.createEstadoCandidato(createDTO);
                });
        verify(estadoCandidatoRepository).findByEstadoCandidato("Existente");
        verify(estadoCandidatoRepository, never()).save(any(EstadoCandidato.class));
    }

    @Test
    void testUpdateEstadoCandidato_Success() {
        // Arrange
        EstadoCandidatoCreateDTO updateDTO = new EstadoCandidatoCreateDTO(null);
        updateDTO.setEstado_candidato("Actualizado");

        EstadoCandidato existing = new EstadoCandidato("Original");
        existing.setId(1L);

        when(estadoCandidatoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(estadoCandidatoRepository.findByEstadoCandidato("Actualizado"))
                .thenReturn(Optional.empty());
        when(estadoCandidatoRepository.save(any(EstadoCandidato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EstadoCandidatoDTO result = estadoCandidatoService.updateEstadoCandidato(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Actualizado", result.getEstado_candidato());
        verify(estadoCandidatoRepository).findById(1L);
        verify(estadoCandidatoRepository).findByEstadoCandidato("Actualizado");
        verify(estadoCandidatoRepository).save(any(EstadoCandidato.class));
    }

    @Test
    void testUpdateEstadoCandidato_NotFound() {
        // Arrange
        EstadoCandidatoCreateDTO updateDTO = new EstadoCandidatoCreateDTO(null);
        updateDTO.setEstado_candidato("Actualizado");

        when(estadoCandidatoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                EstadoCandidatoNotFoundException.class,
                () -> {
                    estadoCandidatoService.updateEstadoCandidato(999L, updateDTO);
                });
        verify(estadoCandidatoRepository).findById(999L);
        verify(estadoCandidatoRepository, never()).save(any(EstadoCandidato.class));
    }

    @Test
    void testUpdateEstadoCandidato_NameConflict() {
        // Arrange
        EstadoCandidatoCreateDTO updateDTO = new EstadoCandidatoCreateDTO(null);
        updateDTO.setEstado_candidato("En Uso");

        EstadoCandidato existing = new EstadoCandidato("Original");
        existing.setId(1L);

        EstadoCandidato conflict = new EstadoCandidato("En Uso");
        conflict.setId(2L);

        when(estadoCandidatoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(estadoCandidatoRepository.findByEstadoCandidato("En Uso"))
                .thenReturn(Optional.of(conflict));

        // Act & Assert
        assertThrows(
                EstadoCandidatoAlreadyExistsException.class,
                () -> {
                    estadoCandidatoService.updateEstadoCandidato(1L, updateDTO);
                });
        verify(estadoCandidatoRepository).findById(1L);
        verify(estadoCandidatoRepository).findByEstadoCandidato("En Uso");
        verify(estadoCandidatoRepository, never()).save(any(EstadoCandidato.class));
    }

    @Test
    void testDeleteEstadoCandidato() {
        // Arrange
        doNothing().when(estadoCandidatoRepository).deleteById(1L);

        // Act
        estadoCandidatoService.deleteEstadoCandidato(1L);

        // Assert
        verify(estadoCandidatoRepository).deleteById(1L);
    }
}
