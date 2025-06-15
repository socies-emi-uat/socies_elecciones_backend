package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.Candidato.CandidatoCreateDTO;
import com.socies.voto.dtos.Candidato.CandidatoDTO;
import com.socies.voto.dtos.Candidato.CandidatoUpdateDTO;
import com.socies.voto.services.CandidatoService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/administrador/candidatos")
public class CandidatoController {

    @Autowired private CandidatoService candidatoService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CandidatoDTO>>> obtenerTodosLosCandidatos() {
        List<CandidatoDTO> candidatos = candidatoService.obtenerTodosLosCandidatos();
        ResponseWrapper<List<CandidatoDTO>> response =
                new ResponseWrapper<>(true, "Candidatos obtenidos correctamente.", candidatos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CandidatoDTO>> obtenerCandidatoPorId(
            @PathVariable Long id) {
        CandidatoDTO candidato = candidatoService.obtenerCandidatoPorId(id);
        ResponseWrapper<CandidatoDTO> response =
                new ResponseWrapper<>(true, "Candidato obtenido correctamente.", candidato);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<CandidatoDTO>> crearCandidato(
            @Valid @RequestBody CandidatoCreateDTO candidatoCreateDTO) {
        CandidatoDTO candidato = candidatoService.crearCandidato(candidatoCreateDTO);
        ResponseWrapper<CandidatoDTO> response =
                new ResponseWrapper<>(true, "Candidato creado correctamente.", candidato);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CandidatoDTO>> actualizarCandidato(
            @PathVariable Long id, @Valid @RequestBody CandidatoUpdateDTO candidatoUpdateDTO) {
        CandidatoDTO candidato = candidatoService.actualizarCandidato(id, candidatoUpdateDTO);
        ResponseWrapper<CandidatoDTO> response =
                new ResponseWrapper<>(true, "Candidato actualizado correctamente.", candidato);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> eliminarCandidato(@PathVariable Long id) {
        candidatoService.eliminarCandidato(id);
        ResponseWrapper<Void> response =
                new ResponseWrapper<>(true, "Candidato eliminado correctamente.", null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
