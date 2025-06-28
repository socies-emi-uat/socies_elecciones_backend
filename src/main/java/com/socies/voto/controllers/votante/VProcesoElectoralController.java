package com.socies.voto.controllers.votante;

import com.socies.voto.dtos.Candidatura.VCandidaturaPublicDTO;
import com.socies.voto.dtos.ProceoElectoral.VProcesoCandidaturasDTO;
import com.socies.voto.models.ProcesoElectoral;
import com.socies.voto.repositories.CandidaturaRepository;
import com.socies.voto.repositories.ProcesoElectoralRepository;
import com.socies.voto.services.ProcesoElectoralService;
import com.socies.voto.utils.ResponseWrapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votante/procesos")
public class VProcesoElectoralController {

    @Autowired private ProcesoElectoralRepository procesoRepository;

    @Autowired private ProcesoElectoralService procesoElectoralService;

    @Autowired private CandidaturaRepository candidaturaRepository;

    // Obtener todos los procesos con sus candidaturas, partido y candidato
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<VProcesoCandidaturasDTO>>>
            getAllProcesosConCandidaturas() {
        List<ProcesoElectoral> procesos = procesoRepository.findAll();

        List<VProcesoCandidaturasDTO> dtos =
                procesos.stream()
                        .map(
                                proceso -> {
                                    List<VCandidaturaPublicDTO> candidaturaDTOs =
                                            candidaturaRepository
                                                    .findByProcesoElectoralId(proceso.getId())
                                                    .stream()
                                                    .map(VCandidaturaPublicDTO::new)
                                                    .collect(Collectors.toList());

                                    return new VProcesoCandidaturasDTO(
                                            proceso.getNombreProceso(),
                                            proceso.getDescripcionProceso(),
                                            proceso.getFechaInicio(),
                                            proceso.getFechaFin(),
                                            candidaturaDTOs);
                                })
                        .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ResponseWrapper<>(true, "Todos los procesos electorales", dtos), HttpStatus.OK);
    }

    // Obtener un proceso específico por ID con sus candidaturas
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<VProcesoCandidaturasDTO>> getProcesoConCandidaturasById(
            @PathVariable Long id) {
        Optional<ProcesoElectoral> procesoOpt = procesoRepository.findById(id);

        if (procesoOpt.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseWrapper<>(false, "Proceso electoral no encontrado", null),
                    HttpStatus.NOT_FOUND);
        }

        ProcesoElectoral proceso = procesoOpt.get();

        List<VCandidaturaPublicDTO> candidaturaDTOs =
                candidaturaRepository.findByProcesoElectoralId(id).stream()
                        .map(VCandidaturaPublicDTO::new)
                        .collect(Collectors.toList());

        VProcesoCandidaturasDTO dto =
                new VProcesoCandidaturasDTO(
                        proceso.getNombreProceso(),
                        proceso.getDescripcionProceso(),
                        proceso.getFechaInicio(),
                        proceso.getFechaFin(),
                        candidaturaDTOs);

        return new ResponseEntity<>(
                new ResponseWrapper<>(true, "Proceso electoral con candidaturas", dto),
                HttpStatus.OK);
    }

    @GetMapping("/activo")
    public ResponseEntity<ResponseWrapper<VProcesoCandidaturasDTO>> getProcesoElectoralActual() {
        VProcesoCandidaturasDTO proceso_electoral_actual =
                procesoElectoralService.getProcesoElecturalEnCurso();
        ResponseWrapper<VProcesoCandidaturasDTO> proceso_electoral =
                new ResponseWrapper<>(true, "Proceso electoral actual", proceso_electoral_actual);
        return new ResponseEntity<>(proceso_electoral, HttpStatus.OK);
    }
}
