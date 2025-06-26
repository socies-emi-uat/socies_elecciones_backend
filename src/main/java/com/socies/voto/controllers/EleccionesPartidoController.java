package com.socies.voto.controllers;

import com.socies.voto.dtos.Partido.PartidoPublicDTO;
import com.socies.voto.services.PartidoService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class EleccionesPartidoController {

    @Autowired PartidoService partidoService;

    @GetMapping("/elecciones")
    @Transactional
    public ResponseEntity<ResponseWrapper<List<PartidoPublicDTO>>> getPartidosConCandidaturas() {

        List<PartidoPublicDTO> partidos = partidoService.getPublicPartidos();

        ResponseWrapper<List<PartidoPublicDTO>> response =
                new ResponseWrapper<>(
                        true,
                        "Partidos con candidaturas públicas cargados correctamente",
                        partidos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
