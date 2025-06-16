package com.socies.voto.services;

import com.socies.voto.dtos.Candidato.CandidatoCreateDTO;
import com.socies.voto.dtos.Candidato.CandidatoDTO;
import com.socies.voto.dtos.Candidato.CandidatoUpdateDTO;
import com.socies.voto.exceptions.Candidato.CandidatoAlreadyExistsException;
import com.socies.voto.exceptions.Candidato.CandidatoNotFoundException;
import com.socies.voto.models.Candidato;
import com.socies.voto.models.Cargo;
import com.socies.voto.models.EstadoCandidato;
import com.socies.voto.repositories.CandidatoRepository;
import com.socies.voto.repositories.CargoRepository;
import com.socies.voto.repositories.EstadoCandidatoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidatoService {

    @Autowired private CandidatoRepository candidatoRepository;
    @Autowired private EstadoCandidatoRepository estadoCandidatoRepository;
    @Autowired private CargoRepository cargoRepository;

    public List<CandidatoDTO> obtenerTodosLosCandidatos() {
        return candidatoRepository.findAll().stream()
                .map(CandidatoDTO::new)
                .collect(Collectors.toList());
    }

    public CandidatoDTO obtenerCandidatoPorId(Long id) {
        return candidatoRepository
                .findById(id)
                .map(CandidatoDTO::new)
                .orElseThrow(
                        () -> new CandidatoNotFoundException("El candidato no fue encontrado"));
    }

    public CandidatoDTO crearCandidato(CandidatoCreateDTO candidatoCreateDTO) {
        // Verificar si la propuesta es requerida
        if (candidatoCreateDTO.getPropuesta() == null
                || candidatoCreateDTO.getPropuesta().isEmpty()) {
            throw new RuntimeException("La propuesta es requerida");
        }
        // Verificar si ya existe por CI
        candidatoRepository
                .findByCedulaIdentidad(candidatoCreateDTO.getCiCandidato())
                .ifPresent(
                        c -> {
                            throw new CandidatoAlreadyExistsException("El candidato ya existe");
                        });
        // Verificar si el correo ya existe
        if (candidatoCreateDTO.getCorreoCandidato() != null
                && !candidatoCreateDTO.getCorreoCandidato().isEmpty()) {
            candidatoRepository
                    .findByCorreo(candidatoCreateDTO.getCorreoCandidato())
                    .ifPresent(
                            c -> {
                                throw new CandidatoAlreadyExistsException(
                                        "El correo electrónico ya está registrado");
                            });
        }

        EstadoCandidato estadoCandidato =
                estadoCandidatoRepository
                        .findById(candidatoCreateDTO.getEstadoCandidatoId())
                        .orElseThrow(
                                () -> new RuntimeException("Estado de candidato no encontrado"));
        Cargo cargo =
                cargoRepository
                        .findById(candidatoCreateDTO.getCargoId())
                        .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        // Crear y guardar nuevo candidato
        Candidato nuevoCandidato = new Candidato();
        nuevoCandidato.setNombreCandidato(candidatoCreateDTO.getNombreCandidato());
        nuevoCandidato.setApellidoPaterno(candidatoCreateDTO.getApPaterno());
        nuevoCandidato.setApellidoMaterno(candidatoCreateDTO.getApMaterno());
        nuevoCandidato.setCedulaIdentidad(candidatoCreateDTO.getCiCandidato());
        nuevoCandidato.setFechaNacimiento(candidatoCreateDTO.getFechaNacimiento());
        nuevoCandidato.setFotoUrl(candidatoCreateDTO.getFotoUrl());
        nuevoCandidato.setCorreo(candidatoCreateDTO.getCorreoCandidato());
        nuevoCandidato.setPropuesta(candidatoCreateDTO.getPropuesta());
        nuevoCandidato.setEstadoCandidato(estadoCandidato);
        nuevoCandidato.setCargo(cargo);
        Candidato guardado = candidatoRepository.save(nuevoCandidato);
        return new CandidatoDTO(guardado);
    }

    public CandidatoDTO actualizarCandidato(Long id, CandidatoUpdateDTO candidatoUpdateDTO) {
        Candidato candidato =
                candidatoRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new CandidatoNotFoundException("Candidato no encontrado"));
        // Verificar si la propuesta es requerida
        if (candidatoUpdateDTO.getPropuesta() == null
                || candidatoUpdateDTO.getPropuesta().isEmpty()) {
            throw new RuntimeException("La propuesta es requerida");
        }
        // Verificar si el CI ya existe (excepto para el mismo registro)
        candidatoRepository
                .findByCedulaIdentidad(candidatoUpdateDTO.getCiCandidato())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(
                        c -> {
                            throw new CandidatoAlreadyExistsException("El CI ya está registrado");
                        });
        // Verificar si el correo ya existe (excepto para el mismo registro)
        if (candidatoUpdateDTO.getCorreoCandidato() != null
                && !candidatoUpdateDTO.getCorreoCandidato().isEmpty()) {
            candidatoRepository
                    .findByCorreo(candidatoUpdateDTO.getCorreoCandidato())
                    .filter(c -> !c.getId().equals(id))
                    .ifPresent(
                            c -> {
                                throw new CandidatoAlreadyExistsException(
                                        "El correo electrónico ya está registrado");
                            });
        }

        EstadoCandidato estadoCandidato =
                estadoCandidatoRepository
                        .findById(candidatoUpdateDTO.getEstadoCandidatoId())
                        .orElseThrow(
                                () -> new RuntimeException("Estado de candidato no encontrado"));
        Cargo cargo =
                cargoRepository
                        .findById(candidatoUpdateDTO.getCargoId())
                        .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        candidato.setNombreCandidato(candidatoUpdateDTO.getNombreCandidato());
        candidato.setApellidoPaterno(candidatoUpdateDTO.getApPaterno());
        candidato.setApellidoMaterno(candidatoUpdateDTO.getApMaterno());
        candidato.setCedulaIdentidad(candidatoUpdateDTO.getCiCandidato());
        candidato.setFechaNacimiento(candidatoUpdateDTO.getFechaNacimiento());
        candidato.setFotoUrl(candidatoUpdateDTO.getFotoUrl());
        candidato.setCorreo(candidatoUpdateDTO.getCorreoCandidato());
        candidato.setPropuesta(candidatoUpdateDTO.getPropuesta());
        candidato.setEstadoCandidato(estadoCandidato);
        candidato.setCargo(cargo);

        Candidato actualizado = candidatoRepository.save(candidato);
        return new CandidatoDTO(actualizado);
    }

    public void eliminarCandidato(Long id) {
        if (!candidatoRepository.existsById(id)) {
            throw new CandidatoNotFoundException("Candidato no encontrado con id " + id);
        }
        candidatoRepository.deleteById(id);
    }
}
