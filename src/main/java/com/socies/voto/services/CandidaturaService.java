package com.socies.voto.services;

import com.socies.voto.dtos.Candidatura.CandidaturaCreateDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Candidatura;
import com.socies.voto.repositories.CandidaturaRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidaturaService {
    @Autowired
    private CandidaturaRepository candidaturaRepository;

    public List<CandidaturaDTO> findAll() {
        return candidaturaRepository.findAll().stream().map(CandidaturaDTO::new).collect(Collectors.toList());
    }

    public CandidaturaDTO obtenerPorId(Long id) {
        return candidaturaRepository.findById(id).map(CandidaturaDTO::new).orElseThrow(() -> new ResourceNotFoundException("Candidatura no existe."));
    }

    public CandidaturaDTO create(CandidaturaCreateDTO dto) {
        Candidatura candidatura = new Candidatura();
        mapCreateDtoToEntity(dto, candidatura);
        candidatura.setEstadoCandidatura(dto.getEstadoCandidatura()); // Estado por defecto
        return new CandidaturaDTO(candidaturaRepository.save(candidatura));
    }

    public CandidaturaDTO update(Long id, CandidaturaUpdateDTO dto) {
        Candidatura candidatura = candidaturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidatura no encontrada con ID: " + id));
        mapUpdateDtoToEntity(dto, candidatura);
        return new CandidaturaDTO(candidaturaRepository.save(candidatura));
    }

    private void mapCreateDtoToEntity(CandidaturaCreateDTO dto, Candidatura entity) {
        entity.setNombreCandidatura(dto.getNombreCandidatura());
        entity.setLema(dto.getLema());
        entity.setCandidato(dto.getCandidato());
        entity.setPartido(dto.getPartido());
        entity.setProcesoElectoral(dto.getProcesoElectoral());
    }


    private void mapUpdateDtoToEntity(CandidaturaUpdateDTO dto, Candidatura entity) {
        if (dto.getNombreCandidatura() != null) {
            entity.setNombreCandidatura(dto.getNombreCandidatura());
        }
        if (dto.getLema() != null) {
            entity.setLema(dto.getLema());
        }
        if (dto.getCandidato() != null) {
            entity.setCandidato(dto.getCandidato());
        }
        if (dto.getPartido() != null) {
            entity.setPartido(dto.getPartido());
        }

    }
}
