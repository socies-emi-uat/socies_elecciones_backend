package com.socies.voto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoCreateDTO;
import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoDTO;
import com.socies.voto.models.EstadoCandidato;
import com.socies.voto.repositories.EstadoCandidatoRepository;
import com.socies.voto.services.EstadoCandidatoServices;

@ExtendWith(MockitoExtension.class)
public class EstadoCandidatoServiceTests {

    @Mock
    private EstadoCandidatoRepository estadoCandidatoRepository;

    @InjectMocks
    private EstadoCandidatoServices estadoCandidatoService;

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
        verify(estadoCandidatoRepository, times(1)).findAll();
    }

    @Test
    void testCreateEstadoCandidato() {
        // Arrange
        EstadoCandidatoCreateDTO createDTO = new EstadoCandidatoCreateDTO(null);
        createDTO.setEstado_candidato("Nuevo Estado");
        
        EstadoCandidato savedEstado = new EstadoCandidato("Nuevo Estado");
        savedEstado.setId(1L);
        
        when(estadoCandidatoRepository.save(any(EstadoCandidato.class))).thenReturn(savedEstado);

        // Act
        EstadoCandidatoDTO result = estadoCandidatoService.createEstadoCandidato(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nuevo Estado", result.getEstado_candidato());
        verify(estadoCandidatoRepository, times(1)).save(any(EstadoCandidato.class));
    }

    @Test
    void testGetEstadoCandidatoById_Exists() {
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
        verify(estadoCandidatoRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEstadoCandidatoById_NotExists() {
        // Arrange
        when(estadoCandidatoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        EstadoCandidatoDTO result = estadoCandidatoService.getEstadoCandidatoById(999L);

        // Assert
        assertNull(result);
        verify(estadoCandidatoRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateEstadoCandidato_Exists() {
        // Arrange
        EstadoCandidatoCreateDTO updateDTO = new EstadoCandidatoCreateDTO(null);
        updateDTO.setEstado_candidato("Actualizado");
        
        EstadoCandidato existingEstado = new EstadoCandidato("Original");
        existingEstado.setId(1L);
        
        when(estadoCandidatoRepository.findById(1L)).thenReturn(Optional.of(existingEstado));
        when(estadoCandidatoRepository.save(any(EstadoCandidato.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EstadoCandidatoDTO result = estadoCandidatoService.updateEstadoCandidato(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Actualizado", result.getEstado_candidato());
        verify(estadoCandidatoRepository, times(1)).findById(1L);
        verify(estadoCandidatoRepository, times(1)).save(any(EstadoCandidato.class));
    }

    @Test
    void testUpdateEstadoCandidato_NotExists() {
        // Arrange
        EstadoCandidatoCreateDTO updateDTO = new EstadoCandidatoCreateDTO(null);
        updateDTO.setEstado_candidato("Actualizado");
        
        when(estadoCandidatoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        EstadoCandidatoDTO result = estadoCandidatoService.updateEstadoCandidato(999L, updateDTO);

        // Assert
        assertNull(result);
        verify(estadoCandidatoRepository, times(1)).findById(999L);
        verify(estadoCandidatoRepository, never()).save(any(EstadoCandidato.class));
    }

    @Test
    void testDeleteEstadoCandidato() {
        // Arrange - no setup needed for simple delete
        
        // Act
        estadoCandidatoService.deleteEstadoCandidato(1L);
        
        // Assert
        verify(estadoCandidatoRepository, times(1)).deleteById(1L);
    }
}