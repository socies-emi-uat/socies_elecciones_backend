package com.socies.voto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.socies.voto.dtos.Candidatura.CandidaturaCreateDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaUpdateDTO;
import com.socies.voto.exceptions.Candidatura.CandidaturaAlreadyExistsException;
import com.socies.voto.exceptions.Candidatura.CandidaturaNotFoundException;
import com.socies.voto.models.Candidato;
import com.socies.voto.models.Candidatura;
import com.socies.voto.models.Cargo;
import com.socies.voto.models.EstadoCandidato;
import com.socies.voto.models.EstadoCandidatura;
import com.socies.voto.models.Partido;
import com.socies.voto.models.ProcesoElectoral;
import com.socies.voto.repositories.CandidatoRepository;
import com.socies.voto.repositories.CandidaturaRepository;
import com.socies.voto.repositories.EstadoCandidaturaRepository;
import com.socies.voto.repositories.PartidoRepository;
import com.socies.voto.repositories.ProcesoElectoralRepository;
import com.socies.voto.services.CandidaturaService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CandidaturaServiceTests {

    @InjectMocks private CandidaturaService candidaturaService;

    @Mock private CandidaturaRepository candidaturaRepository;
    @Mock private CandidatoRepository candidatoRepository;
    @Mock private PartidoRepository partidoRepository;
    @Mock private EstadoCandidaturaRepository estadoCandidaturaRepository;
    @Mock private ProcesoElectoralRepository procesoElectoralRepository;

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
    void create_shouldSaveAndReturnCandidaturaDTO() {
        CandidaturaCreateDTO dto =
                new CandidaturaCreateDTO("Nueva Candidatura", "Por el futuro", 1L, 1L, 1L, 1L);

        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(estadoCandidaturaRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(procesoElectoralRepository.findById(1L)).thenReturn(Optional.of(procesoElectoral));

        Candidatura saved = new Candidatura();
        saved.setId(1L);
        saved.setNombreCandidatura("Nueva Candidatura");
        saved.setLema("Por el futuro");
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
    void create_shouldThrowExceptionIfDuplicateCandidaturaExists() {
        CandidaturaCreateDTO dto =
                new CandidaturaCreateDTO("Nueva Candidatura", "Por el futuro", 1L, 1L, 1L, 1L);

        when(candidaturaRepository.existsByNombreCandidaturaAndPartidoIdAndProcesoElectoralId(
                        dto.getNombreCandidatura(),
                        dto.getPartidoId(),
                        dto.getProcesoElectoralId()))
                .thenReturn(true);

        assertThrows(CandidaturaAlreadyExistsException.class, () -> candidaturaService.create(dto));

        verify(candidaturaRepository, never()).save(any());
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
        dto.setCandidatoId(1L);
        dto.setPartidoId(1L);
        dto.setEstadoCandidaturaId(1L);
        dto.setProcesoElectoralId(1L);

        when(candidaturaRepository.findById(id)).thenReturn(Optional.of(existing));
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));
        when(partidoRepository.findById(1L)).thenReturn(Optional.of(partido));
        when(estadoCandidaturaRepository.findById(1L)).thenReturn(Optional.of(estado));
        when(procesoElectoralRepository.findById(1L)).thenReturn(Optional.of(procesoElectoral));
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

        assertThrows(CandidaturaNotFoundException.class, () -> candidaturaService.update(id, dto));
        verify(candidaturaRepository, never()).save(any());
    }
}
