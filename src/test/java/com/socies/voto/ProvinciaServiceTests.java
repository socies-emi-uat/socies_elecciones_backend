package com.socies.voto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.socies.voto.dtos.Provincia.ProvinciaCreateDTO;
import com.socies.voto.dtos.Provincia.ProvinciaDTO;
import com.socies.voto.dtos.Provincia.ProvinciaUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Departamento;
import com.socies.voto.models.Provincia;
import com.socies.voto.repositories.ProvinciaRepository;
import com.socies.voto.services.ProvinciaService;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ProvinciaServiceTests {

    @InjectMocks private ProvinciaService provinciaService;

    @Mock private ProvinciaRepository provinciaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Provincia provincia = new Provincia();
        provincia.setId(1L);
        provincia.setNombre("Santa Cruz");
        provincia.setDepartamento(new Departamento());

        when(provinciaRepository.findAll()).thenReturn(Collections.singletonList(provincia));

        List<ProvinciaDTO> result = provinciaService.findAll();

        assertEquals(1, result.size());
        assertEquals("Santa Cruz", result.get(0).getNombre());
        verify(provinciaRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdExists() {
        Provincia provincia = new Provincia();
        provincia.setId(1L);
        provincia.setNombre("La Paz");
        provincia.setDepartamento(new Departamento());

        when(provinciaRepository.findById(1L)).thenReturn(Optional.of(provincia));

        ProvinciaDTO result = provinciaService.findById(1L);

        assertNotNull(result);
        assertEquals("La Paz", result.getNombre());
    }

    @Test
    void testFindByIdNotExists() {
        when(provinciaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> provinciaService.findById(99L));
    }

    @Test
    void testCreateProvinciaSuccess() {
        ProvinciaCreateDTO dto = new ProvinciaCreateDTO();
        dto.setNombre("Cochabamba");
        dto.setDepartamento(new Departamento());

        when(provinciaRepository.findByNombre("Cochabamba")).thenReturn(Optional.empty());

        ArgumentCaptor<Provincia> captor = ArgumentCaptor.forClass(Provincia.class);

        ProvinciaDTO result = provinciaService.create(dto);

        verify(provinciaRepository).save(captor.capture());
        Provincia savedProvincia = captor.getValue();

        assertEquals("Cochabamba", savedProvincia.getNombre());
        assertEquals("Cochabamba", result.getNombre());
    }

    @Test
    void testCreateProvinciaDuplicate() {
        ProvinciaCreateDTO dto = new ProvinciaCreateDTO();
        dto.setNombre("Beni");
        dto.setDepartamento(new Departamento());

        when(provinciaRepository.findByNombre("Beni")).thenReturn(Optional.of(new Provincia()));

        assertThrows(ResourceNotFoundException.class, () -> provinciaService.create(dto));
        verify(provinciaRepository, never()).save(any());
    }

    @Test
    void testUpdateProvinciaSuccess() {
        ProvinciaUpdateDTO dto = new ProvinciaUpdateDTO();
        dto.setNombre("Tarija");
        dto.setDepartamento(new Departamento());

        Provincia existing = new Provincia();
        existing.setId(1L);
        existing.setNombre("Antiguo");
        existing.setDepartamento(new Departamento());

        when(provinciaRepository.findByNombre("Tarija")).thenReturn(Optional.empty());
        when(provinciaRepository.findById(1L)).thenReturn(Optional.of(existing));

        ProvinciaDTO result = provinciaService.update(1L, dto);

        assertEquals("Tarija", result.getNombre());
        verify(provinciaRepository).findByNombre("Tarija");
        verify(provinciaRepository).findById(1L);
    }

    @Test
    void testUpdateProvinciaDuplicateName() {
        ProvinciaUpdateDTO dto = new ProvinciaUpdateDTO();
        dto.setNombre("Oruro");
        dto.setDepartamento(new Departamento());

        when(provinciaRepository.findByNombre("Oruro")).thenReturn(Optional.of(new Provincia()));

        assertThrows(ResourceNotFoundException.class, () -> provinciaService.update(1L, dto));
        verify(provinciaRepository, never()).findById(anyLong());
    }

    @Test
    void testUpdateProvinciaNotFound() {
        ProvinciaUpdateDTO dto = new ProvinciaUpdateDTO();
        dto.setNombre("Pando");
        dto.setDepartamento(new Departamento());

        when(provinciaRepository.findByNombre("Pando")).thenReturn(Optional.empty());
        when(provinciaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> provinciaService.update(99L, dto));
    }
}
