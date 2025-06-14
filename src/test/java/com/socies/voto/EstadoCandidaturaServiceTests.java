package com.socies.voto;

import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaCreateDTO;
import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaDTO;
import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaUpdateDTO;
import com.socies.voto.exceptions.ResourceAlreadyExistsException;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.EstadoCandidatura;
import com.socies.voto.repositories.EstadoCandidaturaRepository;
import com.socies.voto.services.EstadoCandidaturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstadoCandidaturaServiceTests {

    @Mock
    private EstadoCandidaturaRepository estadoCandidaturaRepository;

    @InjectMocks
    private EstadoCandidaturaService estadoCandidaturaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsListOfDTOs() {
        EstadoCandidatura e1 = new EstadoCandidatura(1L, "Aceptado");
        EstadoCandidatura e2 = new EstadoCandidatura(2L, "Rechazado");
        when(estadoCandidaturaRepository.findAll()).thenReturn(List.of(e1, e2));

        List<EstadoCandidaturaDTO> result = estadoCandidaturaService.findAll();

        assertEquals(2, result.size());
        assertEquals("Aceptado", result.get(0).getEstadoCandidatura());
    }

    @Test
    void findById_existingId_returnsDTO() {
        EstadoCandidatura estado = new EstadoCandidatura(1L, "Observado");
        when(estadoCandidaturaRepository.findById(1L)).thenReturn(Optional.of(estado));

        EstadoCandidaturaDTO dto = estadoCandidaturaService.findById(1L);

        assertEquals("Observado", dto.getEstadoCandidatura());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(estadoCandidaturaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> estadoCandidaturaService.findById(99L));
    }

    @Test
    void create_validInput_savesAndReturnsDTO() {
        EstadoCandidaturaCreateDTO createDTO = new EstadoCandidaturaCreateDTO("Nuevo Estado");
        when(estadoCandidaturaRepository.findByEstadoCandidatura("Nuevo Estado")).thenReturn(Optional.empty());

        EstadoCandidatura saved = new EstadoCandidatura(1L, "Nuevo Estado");
        when(estadoCandidaturaRepository.save(any())).thenReturn(saved);

        EstadoCandidaturaDTO result = estadoCandidaturaService.create(createDTO);

        assertEquals("Nuevo Estado", result.getEstadoCandidatura());
    }

    @Test
    void create_duplicate_throwsException() {
        EstadoCandidaturaCreateDTO createDTO = new EstadoCandidaturaCreateDTO("Duplicado");
        when(estadoCandidaturaRepository.findByEstadoCandidatura("Duplicado"))
                .thenReturn(Optional.of(new EstadoCandidatura()));

        assertThrows(ResourceAlreadyExistsException.class, () -> estadoCandidaturaService.create(createDTO));
    }

    @Test
    void update_existing_validInput_updatesAndReturnsDTO() {
        EstadoCandidatura existing = new EstadoCandidatura(1L, "Original");
        EstadoCandidaturaUpdateDTO updateDTO = new EstadoCandidaturaUpdateDTO("Modificado");

        when(estadoCandidaturaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(estadoCandidaturaRepository.findByEstadoCandidatura("Modificado")).thenReturn(Optional.empty());
        when(estadoCandidaturaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        EstadoCandidaturaDTO result = estadoCandidaturaService.update(1L, updateDTO);

        assertEquals("Modificado", result.getEstadoCandidatura());
    }

    @Test
    void update_nonExistingId_throwsException() {
        when(estadoCandidaturaRepository.findById(99L)).thenReturn(Optional.empty());

        EstadoCandidaturaUpdateDTO updateDTO = new EstadoCandidaturaUpdateDTO("Nuevo");

        assertThrows(ResourceNotFoundException.class, () -> estadoCandidaturaService.update(99L, updateDTO));
    }

    @Test
    void update_toExistingName_throwsException() {
        EstadoCandidatura current = new EstadoCandidatura(1L, "Actual");
        EstadoCandidatura other = new EstadoCandidatura(2L, "Duplicado");
        EstadoCandidaturaUpdateDTO updateDTO = new EstadoCandidaturaUpdateDTO("Duplicado");

        when(estadoCandidaturaRepository.findById(1L)).thenReturn(Optional.of(current));
        when(estadoCandidaturaRepository.findByEstadoCandidatura("Duplicado")).thenReturn(Optional.of(other));

        assertThrows(ResourceAlreadyExistsException.class, () -> estadoCandidaturaService.update(1L, updateDTO));
    }
}

