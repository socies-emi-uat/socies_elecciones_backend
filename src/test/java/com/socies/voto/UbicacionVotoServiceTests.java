package com.socies.voto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.socies.voto.dtos.UbicacionVoto.UbicacionVotoCreateDTO;
import com.socies.voto.dtos.UbicacionVoto.UbicacionVotoUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Municipio;
import com.socies.voto.models.UbicacionVoto;
import com.socies.voto.repositories.MunicipioRepository;
import com.socies.voto.repositories.UbicacionVotoRepository;
import com.socies.voto.services.UbicacionVotoService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UbicacionVotoServiceTests {

    @InjectMocks private UbicacionVotoService ubicacionVotoService;

    @Mock private UbicacionVotoRepository ubicacionVotoRepository;
    @Mock private MunicipioRepository municipioRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_Success() {
        UbicacionVoto ubicacion = new UbicacionVoto();
        ubicacion.setId(1L);
        ubicacion.setNombreUbicacion("Escuela A");
        ubicacion.setDescripcionUbicacion("Una escuela central");
        ubicacion.setDireccion("Calle Falsa 123");
        ubicacion.setLatitude(-16.5f);
        ubicacion.setLongitude(-68.1f);
        ubicacion.setMunicipio(new Municipio());

        when(ubicacionVotoRepository.findById(1L)).thenReturn(Optional.of(ubicacion));

        var result = ubicacionVotoService.findById(1L);

        assertEquals("Escuela A", result.getNombreUbicacion());
        verify(ubicacionVotoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(ubicacionVotoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ubicacionVotoService.findById(999L));
    }

    @Test
    void testCreate_Success() {
        UbicacionVotoCreateDTO dto = new UbicacionVotoCreateDTO();
        dto.setNombreUbicacion("Escuela A");
        dto.setDescripcionUbicacion("Centro de votación");
        dto.setDireccion("Av. Libertador");
        dto.setLatitude(-17.3f);
        dto.setLongitude(-63.2f);
        Municipio municipio = new Municipio();
        municipio.setId(100L);
        dto.setMunicipio(municipio);

        when(ubicacionVotoRepository.findByNombreUbicacion("Escuela A"))
                .thenReturn(Optional.empty());
        when(municipioRepository.findById(100L)).thenReturn(Optional.of(municipio));

        var result = ubicacionVotoService.create(dto);

        assertEquals("Escuela A", result.getNombreUbicacion());
        verify(ubicacionVotoRepository).save(any(UbicacionVoto.class));
    }

    @Test
    void testCreate_AlreadyExists() {
        UbicacionVotoCreateDTO dto = new UbicacionVotoCreateDTO();
        dto.setNombreUbicacion("Existente");

        when(ubicacionVotoRepository.findByNombreUbicacion("Existente"))
                .thenReturn(Optional.of(new UbicacionVoto()));

        assertThrows(ResourceNotFoundException.class, () -> ubicacionVotoService.create(dto));

        verify(ubicacionVotoRepository, never()).save(any());
    }

    @Test
    void testCreate_MunicipioNotFound() {
        UbicacionVotoCreateDTO dto = new UbicacionVotoCreateDTO();
        dto.setNombreUbicacion("Nueva Ubi");
        dto.setDescripcionUbicacion("Test");
        dto.setDireccion("Calle x");
        dto.setLatitude(0f);
        dto.setLongitude(0f);
        Municipio municipio = new Municipio();
        municipio.setId(404L);
        dto.setMunicipio(municipio);

        when(ubicacionVotoRepository.findByNombreUbicacion("Nueva Ubi"))
                .thenReturn(Optional.empty());
        when(municipioRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ubicacionVotoService.create(dto));
        verify(ubicacionVotoRepository, never()).save(any());
    }

    @Test
    void testUpdate_Success() {
        UbicacionVoto existente = new UbicacionVoto();
        existente.setId(1L);
        existente.setNombreUbicacion("Viejo nombre");

        UbicacionVotoUpdateDTO dto = new UbicacionVotoUpdateDTO();
        dto.setNombreUbicacion("Nuevo nombre");
        dto.setDescripcionUbicacion("Nuevo desc");
        dto.setDireccion("Nueva dir");
        dto.setLatitude(1f);
        dto.setLongitude(1f);
        Municipio municipio = new Municipio();
        municipio.setId(10L);
        dto.setMunicipio(municipio);

        when(ubicacionVotoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(municipioRepository.findById(10L)).thenReturn(Optional.of(municipio));
        when(ubicacionVotoRepository.findByNombreUbicacion("Nuevo nombre"))
                .thenReturn(Optional.empty());

        var result = ubicacionVotoService.update(1L, dto);

        assertEquals("Nuevo nombre", result.getNombreUbicacion());
        verify(ubicacionVotoRepository).save(any());
    }

    @Test
    void testUpdate_NotFound() {
        UbicacionVotoUpdateDTO dto = new UbicacionVotoUpdateDTO();
        dto.setNombreUbicacion("No existe");

        when(ubicacionVotoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ubicacionVotoService.update(1L, dto));
    }

    @Test
    void testUpdate_MunicipioNotFound() {
        UbicacionVoto existente = new UbicacionVoto();
        existente.setId(1L);
        existente.setNombreUbicacion("Original");

        UbicacionVotoUpdateDTO dto = new UbicacionVotoUpdateDTO();
        dto.setNombreUbicacion("Actualizado");
        dto.setDescripcionUbicacion("Desc");
        dto.setDireccion("Dir");
        dto.setLatitude(2f);
        dto.setLongitude(2f);
        Municipio municipio = new Municipio();
        municipio.setId(999L);
        dto.setMunicipio(municipio);

        when(ubicacionVotoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(ubicacionVotoRepository.findByNombreUbicacion("Actualizado"))
                .thenReturn(Optional.empty());
        when(municipioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ubicacionVotoService.update(1L, dto));
        verify(ubicacionVotoRepository, never()).save(any());
    }
}
