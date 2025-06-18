package com.socies.voto.services;

import com.socies.voto.dtos.Candidatura.CandidaturaCreateDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaUpdateDTO;
import com.socies.voto.exceptions.Candidatura.CandidaturaAlreadyExistsException;
import com.socies.voto.exceptions.Candidatura.CandidaturaNotFoundException;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Candidato;
import com.socies.voto.models.Candidatura;
import com.socies.voto.models.EstadoCandidatura;
import com.socies.voto.models.Partido;
import com.socies.voto.models.ProcesoElectoral;
import com.socies.voto.repositories.CandidatoRepository;
import com.socies.voto.repositories.CandidaturaRepository;
import com.socies.voto.repositories.EstadoCandidaturaRepository;
import com.socies.voto.repositories.PartidoRepository;
import com.socies.voto.repositories.ProcesoElectoralRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidaturaService {

    @Autowired private CandidaturaRepository candidaturaRepository;
    @Autowired private CandidatoRepository candidatoRepository;
    @Autowired private PartidoRepository partidoRepository;
    @Autowired private EstadoCandidaturaRepository estadoCandidaturaRepository;
    @Autowired private ProcesoElectoralRepository procesoElectoralRepository;

    public List<CandidaturaDTO> findAll() {
        return candidaturaRepository.findAll().stream()
                .map(CandidaturaDTO::new)
                .collect(Collectors.toList());
    }

    public CandidaturaDTO obtenerPorId(Long id) {
        return candidaturaRepository
                .findById(id)
                .map(CandidaturaDTO::new)
                .orElseThrow(() -> new CandidaturaNotFoundException("Candidatura no existe."));
    }

    public CandidaturaDTO create(CandidaturaCreateDTO dto) {
        boolean exists =
                candidaturaRepository.existsByNombreCandidaturaAndPartidoIdAndProcesoElectoralId(
                        dto.getNombreCandidatura(),
                        dto.getPartidoId(),
                        dto.getProcesoElectoralId());
        if (exists) {
            throw new CandidaturaAlreadyExistsException(
                    "Ya existe una candidatura con los mismos datos.");
        }

        Candidato candidato =
                candidatoRepository
                        .findById(dto.getCandidatoId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Candidato no encontrado"));

        Partido partido =
                partidoRepository
                        .findById(dto.getPartidoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Partido no encontrado"));

        EstadoCandidatura estado =
                estadoCandidaturaRepository
                        .findById(dto.getEstadoCandidaturaId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Estado de candidatura no encontrado"));

        ProcesoElectoral proceso =
                procesoElectoralRepository
                        .findById(dto.getProcesoElectoralId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Proceso electoral no encontrado"));

        Candidatura candidatura = new Candidatura();
        candidatura.setNombreCandidatura(dto.getNombreCandidatura());
        candidatura.setLema(dto.getLema());
        candidatura.setCandidato(candidato);
        candidatura.setPartido(partido);
        candidatura.setEstadoCandidatura(estado);
        candidatura.setProcesoElectoral(proceso);

        return new CandidaturaDTO(candidaturaRepository.save(candidatura));
    }

    public CandidaturaDTO update(Long id, CandidaturaUpdateDTO dto) {
        Candidatura candidatura =
                candidaturaRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new CandidaturaNotFoundException(
                                                "Candidatura no encontrada con ID: " + id));

        if (dto.getNombreCandidatura() != null) {
            candidatura.setNombreCandidatura(dto.getNombreCandidatura());
        }

        if (dto.getLema() != null) {
            candidatura.setLema(dto.getLema());
        }

        if (dto.getCandidatoId() != null) {
            candidatura.setCandidato(
                    candidatoRepository
                            .findById(dto.getCandidatoId())
                            .orElseThrow(
                                    () ->
                                            new ResourceNotFoundException(
                                                    "Candidato no encontrado")));
        }

        if (dto.getPartidoId() != null) {
            candidatura.setPartido(
                    partidoRepository
                            .findById(dto.getPartidoId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Partido no encontrado")));
        }

        if (dto.getEstadoCandidaturaId() != null) {
            candidatura.setEstadoCandidatura(
                    estadoCandidaturaRepository
                            .findById(dto.getEstadoCandidaturaId())
                            .orElseThrow(
                                    () ->
                                            new ResourceNotFoundException(
                                                    "Estado candidatura no encontrado")));
        }

        if (dto.getProcesoElectoralId() != null) {
            candidatura.setProcesoElectoral(
                    procesoElectoralRepository
                            .findById(dto.getProcesoElectoralId())
                            .orElseThrow(
                                    () ->
                                            new ResourceNotFoundException(
                                                    "Proceso electoral no encontrado")));
        }

        return new CandidaturaDTO(candidaturaRepository.save(candidatura));
    }
}
