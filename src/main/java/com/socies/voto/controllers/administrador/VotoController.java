package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.Voto.AVotoDTO;
import com.socies.voto.services.VotoService;
import com.socies.voto.utils.ResponseWrapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/administrador/votos")
public class VotoController {
    @Autowired private VotoService votoService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<AVotoDTO>>> findAll() {
        List<AVotoDTO> votoDTOS = votoService.findAllAdmin();
        ResponseWrapper<List<AVotoDTO>> votos =
                new ResponseWrapper<>(true, "Todos los votos obtenidos", votoDTOS);
        return new ResponseEntity<>(votos, HttpStatus.OK);
    }

    @GetMapping("/id_voto}")
    public ResponseEntity<ResponseWrapper<AVotoDTO>> findById(@PathVariable Long id_voto) {
        AVotoDTO votoDTO = votoService.findByIdAdministrador(id_voto);
        ResponseWrapper<AVotoDTO> voto = new ResponseWrapper<>(true, "Voto encontrado", votoDTO);
        return new ResponseEntity<>(voto, HttpStatus.OK);
    }
}
