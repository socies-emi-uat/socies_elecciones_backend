package com.socies.voto.services;

import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoCreateDTO;
import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoDTO;
import com.socies.voto.exceptions.EstadoCandidato.EstadoCandidatoAlreadyExistsException;
import com.socies.voto.exceptions.EstadoCandidato.EstadoCandidatoNotFoundException;
import com.socies.voto.models.EstadoCandidato;
import com.socies.voto.repositories.EstadoCandidatoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoCandidatoServices {

    @Autowired public EstadoCandidatoRepository estadoCandidatoRepository;

    public List<EstadoCandidatoDTO> getAllEstadoCandidatos() {
        return estadoCandidatoRepository.findAll().stream()
                .map(EstadoCandidatoDTO::new)
                .collect(Collectors.toList());
    }

    public EstadoCandidatoDTO getEstadoCandidatoById(Long id) {
        return estadoCandidatoRepository
                .findById(id)
                .map(EstadoCandidatoDTO::new)
                .orElseThrow(
                        () ->
                                new EstadoCandidatoNotFoundException(
                                        "El estado de candidato no fue encontrado"));
    }

    public EstadoCandidatoDTO createEstadoCandidato(
            EstadoCandidatoCreateDTO estadoCandidatoCreateDTO) {
        // Verificar si ya existe por nombre
        estadoCandidatoRepository
                .findByEstadoCandidato(estadoCandidatoCreateDTO.getEstado_candidato())
                .ifPresent(
                        e -> {
                            throw new EstadoCandidatoAlreadyExistsException(
                                    "El estado de candidato ya existe");
                        });
        // Crear y guardar nuevo estado de candidato
        EstadoCandidato nuevoEstadoCandidato =
                new EstadoCandidato(estadoCandidatoCreateDTO.getEstado_candidato());
        EstadoCandidato guardado = estadoCandidatoRepository.save(nuevoEstadoCandidato);
        // Devolver DTO del estado de candidato recién creado
        return new EstadoCandidatoDTO(guardado.getId(), guardado.getEstadoCandidato());
    }

    public EstadoCandidatoDTO updateEstadoCandidato(Long id, EstadoCandidatoCreateDTO updateDTO) {
        EstadoCandidato estado =
                estadoCandidatoRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new EstadoCandidatoNotFoundException(
                                                "Estado de candidato no encontrado"));
        // Verificar si el nuevo nombre ya existe (excepto para el mismo registro)
        estadoCandidatoRepository
                .findByEstadoCandidato(updateDTO.getEstado_candidato())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(
                        e -> {
                            throw new EstadoCandidatoAlreadyExistsException(
                                    "El nombre del estado ya está en uso");
                        });
        estado.setEstadoCandidato(updateDTO.getEstado_candidato());
        EstadoCandidato actualizado = estadoCandidatoRepository.save(estado);
        return new EstadoCandidatoDTO(actualizado.getId(), actualizado.getEstadoCandidato());
    }

    public void deleteEstadoCandidato(Long id) {
        EstadoCandidato estado =
                estadoCandidatoRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new EstadoCandidatoNotFoundException(
                                                "Estado de candidato no encontrado"));
        // Verificar si el estado está asociado a algún candidato
        if (estado.getCandidatos() != null && !estado.getCandidatos().isEmpty()) {
            throw new IllegalStateException(
                    "No se puede eliminar el estado porque está asociado a candidatos");
        }

        estadoCandidatoRepository.delete(estado);
    }
}
