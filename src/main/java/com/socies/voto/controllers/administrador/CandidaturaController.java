package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.Candidatura.CandidaturaCreateDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaDTO;
import com.socies.voto.dtos.Candidatura.CandidaturaUpdateDTO;
import com.socies.voto.services.CandidaturaService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/administrador/candidaturas")
public class CandidaturaController {

    @Autowired private CandidaturaService candidaturaService;

    // Obtener todas las candidaturas
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CandidaturaDTO>>> findAll() {
        List<CandidaturaDTO> candidaturas = candidaturaService.findAll();
        ResponseWrapper<List<CandidaturaDTO>> response =
                new ResponseWrapper<>(true, "Todas las candidaturas obtenidas.", candidaturas);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Obtener una candidatura por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CandidaturaDTO>> findById(@PathVariable Long id) {
        CandidaturaDTO candidatura = candidaturaService.obtenerPorId(id);
        ResponseWrapper<CandidaturaDTO> response =
                new ResponseWrapper<>(true, "Candidatura obtenida correctamente.", candidatura);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Crear una nueva candidatura
    @PostMapping
    public ResponseEntity<ResponseWrapper<CandidaturaDTO>> create(
            @Valid @RequestBody CandidaturaCreateDTO candidaturaCreateDTO) {
        CandidaturaDTO candidaturaDTO = candidaturaService.create(candidaturaCreateDTO);
        ResponseWrapper<CandidaturaDTO> response =
                new ResponseWrapper<>(true, "Candidatura creada exitosamente.", candidaturaDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Actualizar una candidatura
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CandidaturaDTO>> update(
            @PathVariable Long id, @Valid @RequestBody CandidaturaUpdateDTO candidaturaUpdateDTO) {
        CandidaturaDTO candidaturaDTO = candidaturaService.update(id, candidaturaUpdateDTO);
        ResponseWrapper<CandidaturaDTO> response =
                new ResponseWrapper<>(true, "Candidatura actualizada con éxito.", candidaturaDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
