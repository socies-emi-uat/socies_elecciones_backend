package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoCreateDTO;
import com.socies.voto.dtos.EstadoCandidatos.EstadoCandidatoDTO;
import com.socies.voto.services.EstadoCandidatoServices;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/estados-candidatos")
public class EstadoCandidatoController {

    @Autowired private final EstadoCandidatoServices estadoCandidatoServices;

    public EstadoCandidatoController(EstadoCandidatoServices estadoCandidatoServices) {
        this.estadoCandidatoServices = estadoCandidatoServices;
    }

    @GetMapping
    public ResponseEntity<List<EstadoCandidatoDTO>> getAllEstadoCandidatos() {
        List<EstadoCandidatoDTO> estados = estadoCandidatoServices.getAllEstadoCandidatos();
        return ResponseEntity.ok(estados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoCandidatoDTO> getEstadoCandidatoById(@PathVariable Long id) {
        EstadoCandidatoDTO estado = estadoCandidatoServices.getEstadoCandidatoById(id);
        return ResponseEntity.ok(estado);
    }

    @PostMapping
    public ResponseEntity<EstadoCandidatoDTO> createEstadoCandidato(
            @Valid @RequestBody EstadoCandidatoCreateDTO estadoCandidatoCreateDTO) {
        EstadoCandidatoDTO nuevoEstado =
                estadoCandidatoServices.createEstadoCandidato(estadoCandidatoCreateDTO);
        return ResponseEntity.ok(nuevoEstado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoCandidatoDTO> updateEstadoCandidato(
            @PathVariable Long id, @Valid @RequestBody EstadoCandidatoCreateDTO updateDTO) {
        EstadoCandidatoDTO estadoActualizado =
                estadoCandidatoServices.updateEstadoCandidato(id, updateDTO);
        return ResponseEntity.ok(estadoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadoCandidato(@PathVariable Long id) {
        estadoCandidatoServices.deleteEstadoCandidato(id);
        return ResponseEntity.noContent().build();
    }
}
