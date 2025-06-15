package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.MetodoVoto.MetodoVotoCreateDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoGetVotosDTO;
import com.socies.voto.dtos.MetodoVoto.MetodoVotoUpdateDTO;
import com.socies.voto.services.MetodoVotoService;
import com.socies.voto.utils.ResponseWrapper;
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
@RequestMapping("/api/administrador/metodos-voto")
public class MetodoVotoController {

    @Autowired private MetodoVotoService metodoVotoService;

    // Obtener todos los métodos de voto
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<MetodoVotoDTO>>> getAllMetodosVoto() {
        List<MetodoVotoDTO> metodosVoto = metodoVotoService.findAll();
        ResponseWrapper<List<MetodoVotoDTO>> response =
                new ResponseWrapper<>(
                        true, "Métodos de voto obtenidos correctamente.", metodosVoto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Crear un nuevo método de voto
    @PostMapping
    public ResponseEntity<ResponseWrapper<MetodoVotoDTO>> createMetodoVoto(
            @RequestBody MetodoVotoCreateDTO metodoVotoCreateDTO) {
        MetodoVotoDTO metodoVoto = metodoVotoService.create(metodoVotoCreateDTO);
        ResponseWrapper<MetodoVotoDTO> response =
                new ResponseWrapper<>(true, "Método de voto creado exitosamente.", metodoVoto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Actualizar un método de voto por ID
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<MetodoVotoDTO>> updateMetodoVoto(
            @PathVariable Long id, @RequestBody MetodoVotoUpdateDTO updateDTO) {
        MetodoVotoDTO metodoVotoActualizado = metodoVotoService.update(id, updateDTO);
        ResponseWrapper<MetodoVotoDTO> response =
                new ResponseWrapper<>(
                        true, "Método de voto actualizado exitosamente.", metodoVotoActualizado);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Obtener un método de voto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<MetodoVotoDTO>> getMetodoVotoById(@PathVariable Long id) {
        MetodoVotoDTO metodoVoto = metodoVotoService.findById(id);
        ResponseWrapper<MetodoVotoDTO> response =
                new ResponseWrapper<>(true, "Método de voto encontrado.", metodoVoto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Obtener votos de un método de voto por ID
    @GetMapping("/{id}/votos")
    public ResponseEntity<ResponseWrapper<MetodoVotoGetVotosDTO>> getMetodoVotoWithVotos(
            @PathVariable Long id) {
        MetodoVotoGetVotosDTO metodoVoto = metodoVotoService.findByIdWithVotos(id);
        ResponseWrapper<MetodoVotoGetVotosDTO> response =
                new ResponseWrapper<>(
                        true, "Votos del método de voto obtenidos correctamente.", metodoVoto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Eliminar un método de voto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteMetodoVoto(@PathVariable Long id) {
        metodoVotoService.delete(id);
        ResponseWrapper<String> response =
                new ResponseWrapper<>(true, "Método de voto eliminado exitosamente.", "OK");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
