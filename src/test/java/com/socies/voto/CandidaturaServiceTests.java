package com.socies.voto;

import com.socies.voto.dtos.Candidatura.CandidaturaCreateDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.*;
import com.socies.voto.repositories.CandidaturaRepository;
import com.socies.voto.services.CandidaturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CandidaturaServiceTests {

    @InjectMocks
    private CandidaturaService candidaturaService;

    @Mock
    private CandidaturaRepository candidaturaRepository;

    private Candidato candidato;
    private Partido partido;
    private ProcesoElectoral procesoElectoral;
    private EstadoCandidatura estado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        candidato = new Candidato();
        candidato.setId(1L);
        candidato.setNombreCandidato("Juan Pérez");
        candidato.setEstadoCandidato(new EstadoCandidato());
        candidato.setCargo(new Cargo());

        partido = new Partido();
        partido.setId(1L);
        partido.setNombrePartido("Partido X");

        procesoElectoral = new ProcesoElectoral();
        procesoElectoral.setId(1L);
        procesoElectoral.setNombreProceso("Elecciones 2025");

        estado = new EstadoCandidatura();
        estado.setId(1L);
        estado.setEstadoCandidatura("Activa");
    }

    @Test
    void findAll_shouldReturnAllCandidaturasAsDTO() {
        Candidatura c1 = new Candidatura();
        c1.setId(1L);
        c1.setNombreCandidatura("C1");
        c1.setLema("Lema C1");
        c1.setCandidato(candidato);
        c1.setPartido(partido);
        c1.setProcesoElectoral(procesoElectoral);
        c1.setEstadoCandidatura(estado);

        Candidatura c2 = new Candidatura();
        c2.setId(2L);
        c2.setNombreCandidatura("C2");
        c2.setLema("Lema C2");
        c2.setCandidato(candidato);
        c2.setPartido(partido);
        c2.setProcesoElectoral(procesoElectoral);
        c2.setEstadoCandidatura(estado);

        when(candidaturaRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CandidaturaDTO> result = candidaturaService.findAll();

        assertEquals(2, result.size());
        assertEquals("C1", result.get(0).getNombreCandidatura());
        verify(candidaturaRepository, times(1)).findAll();
    }

    @Test
    void create_shouldSaveAndReturnCandidaturaDTO() {
        CandidaturaCreateDTO dto = new CandidaturaCreateDTO();
        dto.setNombreCandidatura("Nueva Candidatura");
        dto.setLema("Por el futuro");
        dto.setEstadoCandidatura(estado);
        dto.setCandidato(candidato);
        dto.setPartido(partido);
        dto.setProcesoElectoral(procesoElectoral);

        Candidatura saved = new Candidatura();
        saved.setId(1L);
        saved.setNombreCandidatura(dto.getNombreCandidatura());
        saved.setLema(dto.getLema());
        saved.setCandidato(candidato);
        saved.setPartido(partido);
        saved.setProcesoElectoral(procesoElectoral);
        saved.setEstadoCandidatura(estado);

        when(candidaturaRepository.save(any(Candidatura.class))).thenReturn(saved);

        CandidaturaDTO result = candidaturaService.create(dto);

        assertNotNull(result);
        assertEquals("Nueva Candidatura", result.getNombreCandidatura());
        assertEquals("Por el futuro", result.getLema());
        assertEquals("Juan Pérez", result.getCandidato().getNombreCandidato());
        verify(candidaturaRepository, times(1)).save(any(Candidatura.class));
    }

    @Test
    void update_shouldModifyAndReturnUpdatedCandidaturaDTO() {
        Long id = 1L;

        Candidatura existing = new Candidatura();
        existing.setId(id);
        existing.setNombreCandidatura("Antigua");
        existing.setLema("Lema viejo");
        existing.setCandidato(candidato);
        existing.setPartido(partido);
        existing.setProcesoElectoral(procesoElectoral);
        existing.setEstadoCandidatura(estado);

        CandidaturaUpdateDTO dto = new CandidaturaUpdateDTO();
        dto.setNombreCandidatura("Actualizada");
        dto.setLema("Cambio real");
        dto.setCandidato(candidato);
        dto.setPartido(partido);
        dto.setEstadoCandidatura(estado);

        when(candidaturaRepository.findById(id)).thenReturn(Optional.of(existing));
        when(candidaturaRepository.save(any(Candidatura.class))).thenAnswer(i -> i.getArgument(0));

        CandidaturaDTO result = candidaturaService.update(id, dto);

        assertEquals("Actualizada", result.getNombreCandidatura());
        assertEquals("Cambio real", result.getLema());
        verify(candidaturaRepository).save(existing);
    }

    @Test
    void update_shouldThrowExceptionIfCandidaturaNotFound() {
        Long id = 100L;
        CandidaturaUpdateDTO dto = new CandidaturaUpdateDTO();
        when(candidaturaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            candidaturaService.update(id, dto);
        });

        verify(candidaturaRepository, never()).save(any());
    }
}
