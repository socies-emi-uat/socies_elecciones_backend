package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.Candidatura.CandidaturaCreateDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaUpdateDTO;
import com.socies.voto.dtos.EstadoProceso.EstadoProcesoCreateDTO;
import com.socies.voto.dtos.EstadoProceso.EstadoProcesoDTO;
import com.socies.voto.dtos.Provincia.ProvinciaDTO;
import com.socies.voto.dtos.Provincia.ProvinciaUpdateDTO;
import com.socies.voto.services.CandidaturaService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrador/candidaturas")
public class CandidaturaController {

    @Autowired
    private CandidaturaService candidaturaService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CandidaturaDTO>>> findAll() {
        List<CandidaturaDTO> candidaturas = candidaturaService.findAll();
        ResponseWrapper<List<CandidaturaDTO>> candidaturas1 = new ResponseWrapper<>(true, "Todas las candidaturas obtenidas.", candidaturas);
        return new ResponseEntity<>(candidaturas1, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CandidaturaDTO>> findById(Long id) {
        CandidaturaDTO candidatura = candidaturaService.obtenerPorId(id);
        ResponseWrapper<CandidaturaDTO> candidaturas1 = new ResponseWrapper<>(true, "Candidatura obtenida.", candidatura);
        return new ResponseEntity<>(candidaturas1, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<CandidaturaDTO>> create(
            @RequestBody CandidaturaCreateDTO candidaturaCreateDTO) {
        CandidaturaDTO candidaturaDTO = candidaturaService.create(candidaturaCreateDTO);
        ResponseWrapper<CandidaturaDTO> response =
                new ResponseWrapper<>(true, "Candidatura creado exitosamente.", candidaturaDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/id")
    public ResponseEntity<ResponseWrapper<CandidaturaDTO>> update(
            @Valid @RequestBody CandidaturaUpdateDTO candidaturaUpdateDTO,
            @PathVariable Long id) {
        CandidaturaDTO candidaturaDTO = candidaturaService.update(id, candidaturaUpdateDTO);
        ResponseWrapper<CandidaturaDTO> provinciaDTOResponseWrapper =
                new ResponseWrapper<>(true, "Candidatura actualizada con exito.", candidaturaDTO);
        return new ResponseEntity<>(provinciaDTOResponseWrapper, HttpStatus.OK);
    }

}
