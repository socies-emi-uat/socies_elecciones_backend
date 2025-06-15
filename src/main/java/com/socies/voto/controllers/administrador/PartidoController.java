package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.Partido.PartidoCreateDTO;
import com.socies.voto.dtos.Partido.PartidoDTO;
import com.socies.voto.dtos.Partido.PartidoUpdateDTO;
import com.socies.voto.services.PartidoService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administrador/partidos")
public class PartidoController {

    @Autowired private PartidoService partidoService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<PartidoDTO>>> getAll() {
        List<PartidoDTO> partidosDTO = partidoService.findAll();
        ResponseWrapper<List<PartidoDTO>> procesos =
                new ResponseWrapper<>(true, "Lista de todos los partidos.", partidosDTO);

        return new ResponseEntity<>(procesos, HttpStatus.OK);
    }

    @GetMapping("/{id_partido}")
    public ResponseEntity<ResponseWrapper<PartidoDTO>> findById(@PathVariable Long id_partido) {
        PartidoDTO partidoDTO = partidoService.findById(id_partido);
        ResponseWrapper<PartidoDTO> partido =
                new ResponseWrapper<>(true, "Partido encontrado.", partidoDTO);
        return new ResponseEntity<>(partido, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<PartidoDTO>> create(
            @Valid @RequestBody PartidoCreateDTO partidoCreateDTO) {
        PartidoDTO partidoDTO = partidoService.create(partidoCreateDTO);
        ResponseWrapper<PartidoDTO> partido =
                new ResponseWrapper<>(true, "Partido criado.", partidoDTO);
        return new ResponseEntity<>(partido, HttpStatus.CREATED);
    }

    @PutMapping("/{id_partido}")
    public ResponseEntity<ResponseWrapper<PartidoDTO>> update(
            @Valid @RequestBody PartidoUpdateDTO partidoUpdateDTO, @PathVariable Long id_partido) {
        PartidoDTO partidoDTO = partidoService.update(id_partido, partidoUpdateDTO);
        ResponseWrapper<PartidoDTO> partido =
                new ResponseWrapper<>(true, "Partido actualizado correctamente", partidoDTO);

        return new ResponseEntity<>(partido, HttpStatus.OK);
    }

    @PatchMapping("/{id_partido}/estado")
    public ResponseEntity<ResponseWrapper<PartidoDTO>> update(@PathVariable Long id_partido) {
        String mensaje = partidoService.disableOenable(id_partido);
        ResponseWrapper<PartidoDTO> partido = new ResponseWrapper<>(true, mensaje, null);

        return new ResponseEntity<>(partido, HttpStatus.OK);
    }
}
