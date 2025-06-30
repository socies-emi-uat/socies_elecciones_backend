package com.socies.voto.services;

import com.socies.voto.dtos.Candidatura.VCandidaturaPublicDTO;
import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralCreateDTO;
import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralDTO;
import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralUpdateDTO;
import com.socies.voto.dtos.ProceoElectoral.VProcesoCandidaturasDTO;
import com.socies.voto.exceptions.ProcesoElectoral.ProcesoElectoralAlreadyExistsException;
import com.socies.voto.exceptions.ProcesoElectoral.ProcesoElectoralNotFoundException;
import com.socies.voto.models.ProcesoElectoral;
import com.socies.voto.repositories.CandidaturaRepository;
import com.socies.voto.repositories.ProcesoElectoralRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcesoElectoralService {

    @Autowired private ProcesoElectoralRepository procesoElectoralRepository;
    @Autowired private CandidaturaRepository candidaturaRepository;
    @Autowired private BackBlazeService backBlazeService;

    public List<ProcesoElectoralDTO> findAll() {
        return procesoElectoralRepository.findAll().stream()
                .map(ProcesoElectoralDTO::new)
                .collect(Collectors.toList());
    }

    public ProcesoElectoralDTO findById(Long id) {
        return procesoElectoralRepository
                .findById(id)
                .map(ProcesoElectoralDTO::new)
                .orElseThrow(
                        () ->
                                new ProcesoElectoralNotFoundException(
                                        "Proceso electoral no encotrado"));
    }

    public ProcesoElectoralDTO create(ProcesoElectoralCreateDTO procesoElectoralCreateDTO) {

        procesoElectoralRepository
                .findByNombreProceso(procesoElectoralCreateDTO.getNombreProceso())
                .ifPresent(
                        c -> {
                            throw new ProcesoElectoralAlreadyExistsException(
                                    "Proceso electoral ya existe");
                        });

        ProcesoElectoral procesoElectoral = new ProcesoElectoral();

        procesoElectoral.setEstadoProceso(procesoElectoralCreateDTO.getEstadoProceso());
        procesoElectoral.setNombreProceso(procesoElectoralCreateDTO.getNombreProceso());
        procesoElectoral.setDescripcionProceso(procesoElectoralCreateDTO.getDescripcionProceso());
        procesoElectoral.setFechaInicio(procesoElectoralCreateDTO.getFechaInicio());
        procesoElectoral.setFechaFin(procesoElectoralCreateDTO.getFechaFin());

        procesoElectoralRepository.save(procesoElectoral);

        return new ProcesoElectoralDTO(procesoElectoral);
    }

    public ProcesoElectoralDTO actualizar(ProcesoElectoralUpdateDTO dto, Long id) {
        ProcesoElectoral procesoElectoral =
                procesoElectoralRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ProcesoElectoralNotFoundException(
                                                "El proceso electoral no existe."));

        if (dto.getEstadoProceso() != null) {
            procesoElectoral.setEstadoProceso(dto.getEstadoProceso());
        }
        if (dto.getNombreProceso() != null) {
            procesoElectoral.setNombreProceso(dto.getNombreProceso());
        }
        if (dto.getDescripcionProceso() != null) {
            procesoElectoral.setDescripcionProceso(dto.getDescripcionProceso());
        }
        if (dto.getFechaInicio() != null) {
            procesoElectoral.setFechaInicio(dto.getFechaInicio());
        }
        if (dto.getFechaFin() != null) {
            procesoElectoral.setFechaFin(dto.getFechaFin());
        }

        procesoElectoral.setEstadoProceso(dto.getEstadoProceso());
        procesoElectoral.setNombreProceso(dto.getNombreProceso());
        procesoElectoral.setDescripcionProceso(dto.getDescripcionProceso());
        procesoElectoral.setFechaFin(dto.getFechaFin());
        procesoElectoral.setFechaInicio(dto.getFechaInicio());

        procesoElectoralRepository.save(procesoElectoral);

        return new ProcesoElectoralDTO(procesoElectoral);
    }

    public VProcesoCandidaturasDTO getProcesoElecturalEnCurso() {
        List<VProcesoCandidaturasDTO> listaDeProcesosEnCurso =
                procesoElectoralRepository.findAllByEstadoProcesoId(4L).stream()
                        .map(
                                proceso -> {
                                    List<VCandidaturaPublicDTO> candidaturaDTOs =
                                            candidaturaRepository
                                                    .findByProcesoElectoralId(proceso.getId())
                                                    .stream()
                                                    .map(
                                                            candidatura -> {
                                                                String foto_partido =
                                                                        backBlazeService
                                                                                .findFileAsBase64(
                                                                                        candidatura
                                                                                                .getPartido()
                                                                                                .getLogoUrl());

                                                                String foto_candidato =
                                                                        backBlazeService
                                                                                .findFileAsBase64(
                                                                                        candidatura
                                                                                                .getCandidato()
                                                                                                .getFotoUrl());
                                                                return new VCandidaturaPublicDTO(
                                                                        candidatura,
                                                                        foto_candidato,
                                                                        foto_partido);
                                                            })
                                                    .collect(Collectors.toList());

                                    return new VProcesoCandidaturasDTO(
                                            proceso.getNombreProceso(),
                                            proceso.getDescripcionProceso(),
                                            proceso.getFechaInicio(),
                                            proceso.getFechaFin(),
                                            candidaturaDTOs);
                                })
                        .toList();

        if (listaDeProcesosEnCurso.isEmpty()) {
            throw new ProcesoElectoralNotFoundException("No hay procesos en curso.");
        }
        if (listaDeProcesosEnCurso.size() > 1) {
            throw new ProcesoElectoralAlreadyExistsException(
                    "Existen mas de un proceso actual en linea.");
        }
        return listaDeProcesosEnCurso.get(0);
    }
}
