package com.socies.voto;

import com.socies.voto.dtos.Municipio.MunicipioCreateDTO;
import com.socies.voto.dtos.Municipio.MunicipioDTO;
import com.socies.voto.dtos.Municipio.MunicipioUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Municipio;
import com.socies.voto.models.Provincia;
import com.socies.voto.repositories.MunicipioRepository;
import com.socies.voto.services.MunicipioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MunicipioServiceTests {

    @InjectMocks
    private MunicipioService municipioService;

    @Mock
    private MunicipioRepository municipioRepository;

    private AutoCloseable closeable;

    private Provincia provincia;
    private Municipio municipio;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        provincia = new Provincia();
        provincia.setId(1L);
        provincia.setNombre("Provincia Central");

        municipio = new Municipio();
        municipio.setId(1L);
        municipio.setNombre("Municipio A");
        municipio.setProvincia(provincia);
    }

    @Test
    void testFindAll() {
        when(municipioRepository.findAll()).thenReturn(Arrays.asList(municipio));

        List<MunicipioDTO> result = municipioService.findAll();

        assertEquals(1, result.size());
        assertEquals("Municipio A", result.get(0).getNombre());
        verify(municipioRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdExists() {
        when(municipioRepository.findById(1L)).thenReturn(Optional.of(municipio));

        MunicipioDTO result = municipioService.findById(1L);

        assertEquals("Municipio A", result.getNombre());
        assertEquals(provincia, result.getProvincia());
    }

    @Test
    void testFindByIdNotFound() {
        when(municipioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> municipioService.findById(1L));
    }

    @Test
    void testCreateSuccess() {
        MunicipioCreateDTO dto = new MunicipioCreateDTO();
        dto.setNombre("Nuevo Municipio");
        dto.setProvincia(provincia);

        when(municipioRepository.findByNombre("Nuevo Municipio")).thenReturn(Optional.empty());

        MunicipioDTO result = municipioService.create(dto);

        assertEquals("Nuevo Municipio", result.getNombre());
        assertEquals(provincia, result.getProvincia());
        verify(municipioRepository).save(any(Municipio.class));
    }

    @Test
    void testCreateAlreadyExists() {
        MunicipioCreateDTO dto = new MunicipioCreateDTO();
        dto.setNombre("Municipio A");
        dto.setProvincia(provincia);

        when(municipioRepository.findByNombre("Municipio A")).thenReturn(Optional.of(municipio));

        assertThrows(ResourceNotFoundException.class, () -> municipioService.create(dto));
        verify(municipioRepository, never()).save(any());
    }

    @Test
    void testUpdateSuccess() {
        MunicipioUpdateDTO dto = new MunicipioUpdateDTO();
        dto.setNombre("Municipio Actualizado");
        dto.setProvincia(provincia);

        when(municipioRepository.findByNombre("Municipio Actualizado")).thenReturn(Optional.empty());
        when(municipioRepository.findById(1L)).thenReturn(Optional.of(municipio));

        MunicipioDTO result = municipioService.update(1L, dto);

        assertEquals("Municipio Actualizado", result.getNombre());
        assertEquals(provincia, result.getProvincia());
    }

    @Test
    void testUpdateNotFound() {
        MunicipioUpdateDTO dto = new MunicipioUpdateDTO();
        dto.setNombre("Municipio Actualizado");
        dto.setProvincia(provincia);

        when(municipioRepository.findByNombre("Municipio Actualizado")).thenReturn(Optional.empty());
        when(municipioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> municipioService.update(1L, dto));
    }

    @Test
    void testUpdateAlreadyExists() {
        MunicipioUpdateDTO dto = new MunicipioUpdateDTO();
        dto.setNombre("Municipio A"); // ya existe
        dto.setProvincia(provincia);

        when(municipioRepository.findByNombre("Municipio A")).thenReturn(Optional.of(municipio));

        assertThrows(ResourceNotFoundException.class, () -> municipioService.update(1L, dto));
    }
}
