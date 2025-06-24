package com.socies.voto.controllers;

import com.socies.voto.dtos.Candidatura.CandidaturaPublicDTO;
import com.socies.voto.dtos.Partido.PartidoPublicDTO;
import com.socies.voto.models.Candidatura;
import com.socies.voto.models.Partido;
import com.socies.voto.repositories.CandidaturaRepository;
import com.socies.voto.repositories.PartidoRepository;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/elecciones")
public class EleccionesPartidoController {

    private final PartidoRepository partidoRepository;
    private final CandidaturaRepository candidaturaRepository;

    public EleccionesPartidoController(
            PartidoRepository partidoRepository, CandidaturaRepository candidaturaRepository) {
        this.partidoRepository = partidoRepository;
        this.candidaturaRepository = candidaturaRepository;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<ResponseWrapper<List<PartidoPublicDTO>>> getPartidosConCandidaturas() {
        List<Partido> partidos = partidoRepository.findAllByEstadoTrue();

        List<PartidoPublicDTO> partidoDTOs =
                partidos.stream()
                        .map(
                                partido -> {
                                    List<Candidatura> candidaturas =
                                            candidaturaRepository.findByPartidoId(partido.getId());

                                    List<CandidaturaPublicDTO> candidaturaDTOs =
                                            candidaturas.stream()
                                                    .map(CandidaturaPublicDTO::new)
                                                    .collect(Collectors.toList());

                                    return new PartidoPublicDTO(partido, candidaturaDTOs);
                                })
                        .collect(Collectors.toList());

        ResponseWrapper<List<PartidoPublicDTO>> response =
                new ResponseWrapper<>(
                        true,
                        "Partidos con candidaturas públicas cargados correctamente",
                        partidoDTOs);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
