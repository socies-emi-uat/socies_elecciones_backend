package com.socies.voto.services;

import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaCreateDTO;
import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaDTO;
import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaUpdateDTO;
import com.socies.voto.exceptions.ResourceAlreadyExistsException;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.EstadoCandidatura;
import com.socies.voto.repositories.EstadoCandidaturaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoCandidaturaService {

    @Autowired private EstadoCandidaturaRepository estadoCandidaturaRepository;

    public List<EstadoCandidaturaDTO> findAll() {
        return estadoCandidaturaRepository.findAll().stream()
                .map(EstadoCandidaturaDTO::new)
                .collect(Collectors.toList());
    }

    public EstadoCandidaturaDTO findById(Long id) {
        return estadoCandidaturaRepository
                .findById(id)
                .map(EstadoCandidaturaDTO::new)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Estado candidatura no encontrado."));
    }

    public EstadoCandidaturaDTO create(EstadoCandidaturaCreateDTO estadoCandidaturaCreateDTO) {
        estadoCandidaturaRepository
                .findByEstadoCandidatura(estadoCandidaturaCreateDTO.getEstadoCandidatura())
                .ifPresent(
                        estadoCandidatura -> {
                            throw new ResourceAlreadyExistsException("El recurso ya existe.");
                        });

        EstadoCandidatura estadoCandidatura = new EstadoCandidatura();
        estadoCandidatura.setEstadoCandidatura(estadoCandidaturaCreateDTO.getEstadoCandidatura());
        estadoCandidaturaRepository.save(estadoCandidatura);

        return new EstadoCandidaturaDTO(estadoCandidatura);
    }

    public EstadoCandidaturaDTO update(
            Long id, EstadoCandidaturaUpdateDTO estadoCandidaturaUpdateDTO) {
        EstadoCandidatura estadoCandidatura =
                estadoCandidaturaRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Estado candidatura no existe."));

        estadoCandidaturaRepository
                .findByEstadoCandidatura(estadoCandidaturaUpdateDTO.getEstadoCandidatura())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(
                        e -> {
                            throw new ResourceAlreadyExistsException("El recurso ya existe.");
                        });

        estadoCandidatura.setEstadoCandidatura(estadoCandidaturaUpdateDTO.getEstadoCandidatura());
        estadoCandidaturaRepository.save(estadoCandidatura);

        return new EstadoCandidaturaDTO(estadoCandidatura);
    }
}
