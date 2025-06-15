package com.socies.voto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.socies.voto.dtos.MetodoVoto.MetodoVotoCreateDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoGetVotosDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoUpdateDTO;
import com.socies.voto.exceptions.MetodoVoto.MetodoVotoAlreadyExistsException;
import com.socies.voto.exceptions.MetodoVoto.MetodoVotoNotFoundException;
import com.socies.voto.models.MetodoVoto;
import com.socies.voto.repositories.MetodoVotoRepository;
import com.socies.voto.services.MetodoVotoService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MetodoVotoServiceTest {

    @Mock private MetodoVotoRepository metodoVotoRepository;

    @InjectMocks private MetodoVotoService metodoVotoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsListOfDTOs() {
        MetodoVoto m1 = new MetodoVoto("Voto presencial");
        m1.setId(1L);
        MetodoVoto m2 = new MetodoVoto("Voto electrónico");
        m2.setId(2L);

        when(metodoVotoRepository.findAll()).thenReturn(List.of(m1, m2));

        List<MetodoVotoDTO> result = metodoVotoService.findAll();

        assertEquals(2, result.size());
        assertEquals("Voto presencial", result.get(0).getNombre());
    }

    @Test
    void findById_existingId_returnsDTO() {
        MetodoVoto metodoVoto = new MetodoVoto("Voto presencial");
        metodoVoto.setId(1L);
        when(metodoVotoRepository.findById(1L)).thenReturn(Optional.of(metodoVoto));

        MetodoVotoDTO result = metodoVotoService.findById(1L);

        assertEquals("Voto presencial", result.getNombre());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(metodoVotoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MetodoVotoNotFoundException.class, () -> metodoVotoService.findById(99L));
    }

    @Test
    void create_validInput_savesAndReturnsDTO() {
        MetodoVotoCreateDTO createDTO = new MetodoVotoCreateDTO("Voto presencial");
        when(metodoVotoRepository.findByNombre("Voto presencial")).thenReturn(Optional.empty());
        MetodoVoto saved = new MetodoVoto("Voto presencial");
        saved.setId(1L);
        when(metodoVotoRepository.save(any())).thenReturn(saved);

        MetodoVotoDTO result = metodoVotoService.create(createDTO);

        assertEquals("Voto presencial", result.getNombre());
    }

    @Test
    void create_duplicate_throwsException() {
        MetodoVotoCreateDTO createDTO = new MetodoVotoCreateDTO("Voto presencial");
        when(metodoVotoRepository.findByNombre("Voto presencial"))
                .thenReturn(Optional.of(new MetodoVoto()));

        assertThrows(
                MetodoVotoAlreadyExistsException.class, () -> metodoVotoService.create(createDTO));
    }

    @Test
    void update_existing_validInput_updatesAndReturnsDTO() {
        MetodoVoto existing = new MetodoVoto("Voto presencial");
        existing.setId(1L);
        MetodoVotoUpdateDTO updateDTO = new MetodoVotoUpdateDTO("Voto electrónico");
        when(metodoVotoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(metodoVotoRepository.findByNombre("Voto electrónico")).thenReturn(Optional.empty());
        when(metodoVotoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        MetodoVotoDTO result = metodoVotoService.update(1L, updateDTO);

        assertEquals("Voto electrónico", result.getNombre());
    }

    @Test
    void update_nonExistingId_throwsException() {
        when(metodoVotoRepository.findById(99L)).thenReturn(Optional.empty());
        MetodoVotoUpdateDTO updateDTO = new MetodoVotoUpdateDTO("Voto electrónico");

        assertThrows(
                MetodoVotoNotFoundException.class, () -> metodoVotoService.update(99L, updateDTO));
    }

    @Test
    void update_toExistingName_throwsException() {
        MetodoVoto existing = new MetodoVoto("Voto presencial");
        existing.setId(1L);
        MetodoVoto other = new MetodoVoto("Voto electrónico");
        other.setId(2L);
        MetodoVotoUpdateDTO updateDTO = new MetodoVotoUpdateDTO("Voto electrónico");
        when(metodoVotoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(metodoVotoRepository.findByNombre("Voto electrónico")).thenReturn(Optional.of(other));

        assertThrows(
                MetodoVotoAlreadyExistsException.class,
                () -> metodoVotoService.update(1L, updateDTO));
    }

    @Test
    void findByIdWithVotos_existingId_returnsDTOWithVotos() {
        MetodoVoto metodoVoto = new MetodoVoto("Voto presencial");
        metodoVoto.setId(1L);
        when(metodoVotoRepository.findById(1L)).thenReturn(Optional.of(metodoVoto));

        MetodoVotoGetVotosDTO result = metodoVotoService.findByIdWithVotos(1L);

        assertEquals("Voto presencial", result.getNombre());
    }

    @Test
    void findByIdWithVotos_nonExistingId_throwsException() {
        when(metodoVotoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                MetodoVotoNotFoundException.class, () -> metodoVotoService.findByIdWithVotos(99L));
    }

    @Test
    void delete_existingId_deletesMetodoVoto() {
        MetodoVoto metodoVoto = new MetodoVoto("Voto presencial");
        metodoVoto.setId(1L);
        when(metodoVotoRepository.findById(1L)).thenReturn(Optional.of(metodoVoto));

        metodoVotoService.delete(1L);

        verify(metodoVotoRepository, times(1)).delete(metodoVoto);
    }

    @Test
    void delete_nonExistingId_throwsException() {
        when(metodoVotoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MetodoVotoNotFoundException.class, () -> metodoVotoService.delete(99L));
    }
}
