package com.socies.voto.services;

import com.socies.voto.dtos.MetodoVoto.MetodoVotoCreateDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoGetVotosDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoUpdateDTO;
import com.socies.voto.exceptions.MetodoVoto.MetodoVotoAlreadyExistsException;
import com.socies.voto.exceptions.MetodoVoto.MetodoVotoNotFoundException;
import com.socies.voto.models.MetodoVoto;
import com.socies.voto.repositories.MetodoVotoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetodoVotoService {

    @Autowired private MetodoVotoRepository metodoVotoRepository;

    public List<MetodoVotoDTO> findAll() {
        return metodoVotoRepository.findAll().stream()
                .map(MetodoVotoDTO::new)
                .collect(Collectors.toList());
    }

    public MetodoVotoDTO findById(Long id) {
        return metodoVotoRepository
                .findById(id)
                .map(MetodoVotoDTO::new)
                .orElseThrow(
                        () -> new MetodoVotoNotFoundException("Método de voto no encontrado."));
    }

    public MetodoVotoDTO create(MetodoVotoCreateDTO metodoVotoCreateDTO) {
        metodoVotoRepository
                .findByNombre(metodoVotoCreateDTO.getNombre())
                .ifPresent(
                        metodoVoto -> {
                            throw new MetodoVotoAlreadyExistsException(
                                    "El método de voto ya existe.");
                        });
        MetodoVoto metodoVoto = new MetodoVoto();
        metodoVoto.setNombre(metodoVotoCreateDTO.getNombre());
        metodoVotoRepository.save(metodoVoto);
        return new MetodoVotoDTO(metodoVoto);
    }

    public MetodoVotoDTO update(Long id, MetodoVotoUpdateDTO metodoVotoUpdateDTO) {
        MetodoVoto metodoVoto =
                metodoVotoRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new MetodoVotoNotFoundException("Método de voto no existe."));
        metodoVotoRepository
                .findByNombre(metodoVotoUpdateDTO.getNombre())
                .filter(m -> !m.getId().equals(id))
                .ifPresent(
                        m -> {
                            throw new MetodoVotoAlreadyExistsException(
                                    "El método de voto ya existe.");
                        });
        metodoVoto.setNombre(metodoVotoUpdateDTO.getNombre());
        metodoVotoRepository.save(metodoVoto);
        return new MetodoVotoDTO(metodoVoto);
    }

    public MetodoVotoGetVotosDTO findByIdWithVotos(Long id) {
        MetodoVoto metodoVoto =
                metodoVotoRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new MetodoVotoNotFoundException(
                                                "Método de voto no encontrado."));
        // Al acceder a los votos, JPA los cargará automáticamente
        return new MetodoVotoGetVotosDTO(metodoVoto);
    }

    public void delete(Long id) {
        MetodoVoto metodoVoto =
                metodoVotoRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new MetodoVotoNotFoundException(
                                                "Método de voto no encontrado."));
        metodoVotoRepository.delete(metodoVoto);
    }
}
