package com.socies.voto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.socies.voto.dtos.Departamento.DepartamentoCreateDTO;
import com.socies.voto.dtos.Departamento.DepartamentoDTO;
import com.socies.voto.dtos.Departamento.DepartamentoUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Departamento;
import com.socies.voto.repositories.DepartamentoRepository;
import com.socies.voto.services.DepartamentoService;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class DepartamentoServiceTests {

    @InjectMocks private DepartamentoService departamentoService;

    @Mock private DepartamentoRepository departamentoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_ReturnsList() {
        Departamento dep1 = new Departamento();
        dep1.setId(1L);
        dep1.setNombre("La Paz");

        Departamento dep2 = new Departamento();
        dep2.setId(2L);
        dep2.setNombre("Cochabamba");

        when(departamentoRepository.findAll()).thenReturn(List.of(dep1, dep2));

        List<DepartamentoDTO> result = departamentoService.findAll();

        assertEquals(2, result.size());
        assertEquals("La Paz", result.get(0).getNombre());
    }

    @Test
    void testFindById_WhenExists_ReturnsDTO() {
        Departamento dep = new Departamento();
        dep.setId(1L);
        dep.setNombre("La Paz");

        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(dep));

        DepartamentoDTO dto = departamentoService.findById(1L);

        assertEquals("La Paz", dto.getNombre());
    }

    @Test
    void testFindById_WhenNotExists_ThrowsException() {
        when(departamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> departamentoService.findById(99L));
    }

    @Test
    void testCreate_WhenNotExists_CreatesAndReturnsDTO() {
        DepartamentoCreateDTO createDTO = new DepartamentoCreateDTO();
        createDTO.setNombre("Santa Cruz");

        when(departamentoRepository.findByNombre("Santa Cruz")).thenReturn(Optional.empty());

        Departamento saved = new Departamento();
        saved.setId(1L);
        saved.setNombre("Santa Cruz");

        when(departamentoRepository.save(any(Departamento.class))).thenReturn(saved);

        DepartamentoDTO result = departamentoService.create(createDTO);

        assertEquals("Santa Cruz", result.getNombre());
    }

    @Test
    void testCreate_WhenAlreadyExists_ThrowsException() {
        DepartamentoCreateDTO createDTO = new DepartamentoCreateDTO();
        createDTO.setNombre("Santa Cruz");

        Departamento existing = new Departamento();
        existing.setId(1L);
        existing.setNombre("Santa Cruz");

        when(departamentoRepository.findByNombre("Santa Cruz")).thenReturn(Optional.of(existing));

        assertThrows(ResourceNotFoundException.class, () -> departamentoService.create(createDTO));
    }

    @Test
    void testUpdate_WhenValid_UpdatesAndReturnsDTO() {
        DepartamentoUpdateDTO updateDTO = new DepartamentoUpdateDTO();
        updateDTO.setNombre("Tarija");

        Departamento existing = new Departamento();
        existing.setId(1L);
        existing.setNombre("Oruro");

        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(departamentoRepository.findByNombre("Tarija")).thenReturn(Optional.empty());

        Departamento updated = new Departamento();
        updated.setId(1L);
        updated.setNombre("Tarija");

        when(departamentoRepository.save(any(Departamento.class))).thenReturn(updated);

        DepartamentoDTO result = departamentoService.update(updateDTO, 1L);

        assertEquals("Tarija", result.getNombre());
    }

    @Test
    void testUpdate_WhenNombreExists_ThrowsException() {
        DepartamentoUpdateDTO updateDTO = new DepartamentoUpdateDTO();
        updateDTO.setNombre("Tarija");

        Departamento current = new Departamento();
        current.setId(1L);
        current.setNombre("Oruro");

        Departamento duplicate = new Departamento();
        duplicate.setId(2L);
        duplicate.setNombre("Tarija");

        when(departamentoRepository.findById(1L)).thenReturn(Optional.of(current));
        when(departamentoRepository.findByNombre("Tarija")).thenReturn(Optional.of(duplicate));

        assertThrows(
                ResourceNotFoundException.class, () -> departamentoService.update(updateDTO, 1L));
    }

    @Test
    void testUpdate_WhenIdNotFound_ThrowsException() {
        DepartamentoUpdateDTO updateDTO = new DepartamentoUpdateDTO();
        updateDTO.setNombre("Tarija");

        when(departamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class, () -> departamentoService.update(updateDTO, 99L));
    }
}
