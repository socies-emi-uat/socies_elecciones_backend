package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.UbicacionVoto.UbicacionVotoCreateDTO;
import com.socies.voto.dtos.UbicacionVoto.UbicacionVotoDTO;
import com.socies.voto.dtos.UbicacionVoto.UbicacionVotoUpdateDTO;
import com.socies.voto.services.UbicacionVotoService;
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
@RequestMapping("/api/administrador/ubicaciones-voto")
public class UbicacionVotoController {

    @Autowired private UbicacionVotoService ubicacionVotoService;

    // GET ALL
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<UbicacionVotoDTO>>> findAll() {
        List<UbicacionVotoDTO> ubicaciones = ubicacionVotoService.findAll();
        return new ResponseEntity<>(
                new ResponseWrapper<>(true, "Ubicaciones de voto encontradas.", ubicaciones),
                HttpStatus.OK);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<UbicacionVotoDTO>> findById(@PathVariable Long id) {
        UbicacionVotoDTO ubicacion = ubicacionVotoService.findById(id);
        return new ResponseEntity<>(
                new ResponseWrapper<>(true, "Ubicación de voto encontrada.", ubicacion),
                HttpStatus.OK);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ResponseWrapper<UbicacionVotoDTO>> create(
            @Valid @RequestBody UbicacionVotoCreateDTO dto) {
        UbicacionVotoDTO creada = ubicacionVotoService.create(dto);
        return new ResponseEntity<>(
                new ResponseWrapper<>(true, "Ubicación de voto creada con éxito.", creada),
                HttpStatus.CREATED);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<UbicacionVotoDTO>> update(
            @PathVariable Long id, @Valid @RequestBody UbicacionVotoUpdateDTO dto) {
        UbicacionVotoDTO actualizada = ubicacionVotoService.update(id, dto);
        return new ResponseEntity<>(
                new ResponseWrapper<>(
                        true, "Ubicación de voto actualizada con éxito.", actualizada),
                HttpStatus.OK);
    }
}
