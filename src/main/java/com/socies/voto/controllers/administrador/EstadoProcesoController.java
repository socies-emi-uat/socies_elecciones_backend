package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.EstadoProceso.EstadoProcesoCreateDTO;
import com.socies.voto.dtos.EstadoProceso.EstadoProcesoDTO;
import com.socies.voto.dtos.EstadoProceso.EstadoProcesoUpdateDTO;
import com.socies.voto.services.EstadoProcesoService;
import com.socies.voto.utils.ResponseWrapper;
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
@RequestMapping("/api/administrador/estados-proceso")
public class EstadoProcesoController {

    @Autowired private EstadoProcesoService estadoProcesoService;

    // Obtener todos los estados de proceso
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<EstadoProcesoDTO>>> getAllEstadosProceso() {
        List<EstadoProcesoDTO> estados = estadoProcesoService.obtenerTodosLosEstadosProceso();
        ResponseWrapper<List<EstadoProcesoDTO>> response =
                new ResponseWrapper<>(true, "Estados de proceso obtenidos correctamente.", estados);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Crear un nuevo estado de proceso
    @PostMapping
    public ResponseEntity<ResponseWrapper<EstadoProcesoDTO>> createEstadoProceso(
            @RequestBody EstadoProcesoCreateDTO estadoProcesoCreateDTO) {
        EstadoProcesoDTO estado = estadoProcesoService.crearEstadoProceso(estadoProcesoCreateDTO);
        ResponseWrapper<EstadoProcesoDTO> response =
                new ResponseWrapper<>(true, "Estado de proceso creado exitosamente.", estado);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // actualizar un estado de proceso por ID
    @PutMapping("/{id_estado}")
    public ResponseEntity<ResponseWrapper<EstadoProcesoDTO>> actualizarEstadoProceso(
            @PathVariable Long id_estado, @RequestBody EstadoProcesoUpdateDTO updateDTO) {

        EstadoProcesoDTO estadoActualizado =
                estadoProcesoService.actualizarEstadoProceso(id_estado, updateDTO);

        ResponseWrapper<EstadoProcesoDTO> response =
                new ResponseWrapper<>(
                        true, "Estado de proceso actualizado exitosamente.", estadoActualizado);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Obtener un estado de proceso por ID
    @GetMapping("/{id_estado}")
    public ResponseEntity<ResponseWrapper<EstadoProcesoDTO>> getEstadoProcesoById(
            @PathVariable Long id_estado) {
        EstadoProcesoDTO estado = estadoProcesoService.obtenerEstadoProcesoPorId(id_estado);
        ResponseWrapper<EstadoProcesoDTO> response =
                new ResponseWrapper<>(true, "Estado de proceso encontrado.", estado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
