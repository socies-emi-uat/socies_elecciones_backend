package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoCreateDTO;
import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoDTO;
import com.socies.voto.services.EstadoCandidatoServices;
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
@RequestMapping("/api/administrador/estados-candidatos")
public class EstadoCandidatoController {

    @Autowired private EstadoCandidatoServices estadoCandidatoServices;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<EstadoCandidatoDTO>>> getAllEstadoCandidatos() {
        List<EstadoCandidatoDTO> estados = estadoCandidatoServices.getAllEstadoCandidatos();
        ResponseWrapper<List<EstadoCandidatoDTO>> response =
                new ResponseWrapper<>(
                        true, "Estados de candidatos obtenidos correctamente.", estados);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<EstadoCandidatoDTO>> getEstadoCandidatoById(
            @PathVariable Long id) {
        EstadoCandidatoDTO estado = estadoCandidatoServices.getEstadoCandidatoById(id);
        ResponseWrapper<EstadoCandidatoDTO> response =
                new ResponseWrapper<>(true, "Estado de candidato obtenido correctamente.", estado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<EstadoCandidatoDTO>> createEstadoCandidato(
            @Valid @RequestBody EstadoCandidatoCreateDTO estadoCandidatoCreateDTO) {
        EstadoCandidatoDTO nuevoEstado =
                estadoCandidatoServices.createEstadoCandidato(estadoCandidatoCreateDTO);
        ResponseWrapper<EstadoCandidatoDTO> response =
                new ResponseWrapper<>(
                        true, "Estado de candidato creado correctamente.", nuevoEstado);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<EstadoCandidatoDTO>> updateEstadoCandidato(
            @PathVariable Long id, @Valid @RequestBody EstadoCandidatoCreateDTO updateDTO) {
        EstadoCandidatoDTO estadoActualizado =
                estadoCandidatoServices.updateEstadoCandidato(id, updateDTO);
        ResponseWrapper<EstadoCandidatoDTO> response =
                new ResponseWrapper<>(
                        true, "Estado de candidato actualizado correctamente.", estadoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteEstadoCandidato(@PathVariable Long id) {
        estadoCandidatoServices.deleteEstadoCandidato(id);
        ResponseWrapper<Void> response =
                new ResponseWrapper<>(true, "Estado de candidato eliminado correctamente.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
