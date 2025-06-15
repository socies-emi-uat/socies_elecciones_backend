package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaCreateDTO;
import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaDTO;
import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaUpdateDTO;
import com.socies.voto.services.EstadoCandidaturaService;
import com.socies.voto.utils.ResponseWrapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administrador/estados-candidaturas")
public class EstadoCandidaturaController {

    @Autowired private EstadoCandidaturaService estadoCandidaturaService;

    // Obtener todos los estados de proceso
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<EstadoCandidaturaDTO>>> getAllEstadosProceso() {
        List<EstadoCandidaturaDTO> estados = estadoCandidaturaService.findAll();
        ResponseWrapper<List<EstadoCandidaturaDTO>> response =
                new ResponseWrapper<>(
                        true, "Estados de candidaturas obtenidos correctamente.", estados);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Crear un nuevo estado de proceso
    @PostMapping
    public ResponseEntity<ResponseWrapper<EstadoCandidaturaDTO>> createEstadoProceso(
            @RequestBody EstadoCandidaturaCreateDTO estadoCandidaturaCreateDTO) {
        EstadoCandidaturaDTO estado = estadoCandidaturaService.create(estadoCandidaturaCreateDTO);
        ResponseWrapper<EstadoCandidaturaDTO> response =
                new ResponseWrapper<>(true, "Estado de candidaturas creado exitosamente.", estado);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // actualizar un estado de proceso por ID
    @PutMapping("/{id_estado}")
    public ResponseEntity<ResponseWrapper<EstadoCandidaturaDTO>> actualizarEstadoProceso(
            @PathVariable Long id_estado, @RequestBody EstadoCandidaturaUpdateDTO updateDTO) {

        EstadoCandidaturaDTO estadoActualizado =
                estadoCandidaturaService.update(id_estado, updateDTO);

        ResponseWrapper<EstadoCandidaturaDTO> response =
                new ResponseWrapper<>(
                        true, "Estado de candidatura actualizado exitosamente.", estadoActualizado);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Obtener un estado de proceso por ID
    @GetMapping("/{id_estado}")
    public ResponseEntity<ResponseWrapper<EstadoCandidaturaDTO>> getEstadoProcesoById(
            @PathVariable Long id_estado) {
        EstadoCandidaturaDTO estado = estadoCandidaturaService.findById(id_estado);
        ResponseWrapper<EstadoCandidaturaDTO> response =
                new ResponseWrapper<>(true, "Estado de candidatura encontrado.", estado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
