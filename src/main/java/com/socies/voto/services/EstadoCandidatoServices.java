package com.socies.voto.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoCreateDTO;
import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoDTO;
import com.socies.voto.models.EstadoCandidato;
import com.socies.voto.repositories.EstadoCandidatoRepository;

@Service
public class EstadoCandidatoServices {

    @Autowired
    private EstadoCandidatoRepository estadoCandidatoRepository;

    public List<EstadoCandidatoDTO> getAllEstadoCandidatos() {
        return estadoCandidatoRepository.findAll().stream()
                .map(estadoCandidato -> new EstadoCandidatoDTO(estadoCandidato.getId(), estadoCandidato.getEstado_candidato()))
                .collect(Collectors.toList());

    }

    public EstadoCandidatoDTO createEstadoCandidato(EstadoCandidatoCreateDTO estadoCandidatoCreateDTO) {
        EstadoCandidato estadoCandidato = new EstadoCandidato(estadoCandidatoCreateDTO.getEstado_candidato());
        estadoCandidato = estadoCandidatoRepository.save(estadoCandidato);
        return new EstadoCandidatoDTO(estadoCandidato.getId(), estadoCandidato.getEstado_candidato());
    }

    public EstadoCandidatoDTO getEstadoCandidatoById(Long id) {
        EstadoCandidato estadoCandidato = estadoCandidatoRepository.findById(id).orElse(null);
        if (estadoCandidato != null) {
            return new EstadoCandidatoDTO(estadoCandidato.getId(), estadoCandidato.getEstado_candidato());
        } else {
            return null;
        }
    }

    public EstadoCandidatoDTO updateEstadoCandidato(Long id, EstadoCandidatoCreateDTO estadoCandidatoCreateDTO) {
        EstadoCandidato estadoCandidato = estadoCandidatoRepository.findById(id).orElse(null);
        if (estadoCandidato != null) {
            estadoCandidato.setEstado_candidato(estadoCandidatoCreateDTO.getEstado_candidato());
            estadoCandidato = estadoCandidatoRepository.save(estadoCandidato);
            return new EstadoCandidatoDTO(estadoCandidato.getId(), estadoCandidato.getEstado_candidato());
        } else {
            return null;
        }
    }

    public void deleteEstadoCandidato(Long id) {
        estadoCandidatoRepository.deleteById(id);
    }
}
