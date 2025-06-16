package com.socies.voto.controllers.votante;

import com.socies.voto.dtos.Voto.UVotoCreateDTO;
import com.socies.voto.dtos.Voto.UVotoDTO;
import com.socies.voto.services.VotoService;
import com.socies.voto.utils.ResponseWrapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votante/votos")
public class VVotoController {
    @Autowired private VotoService votoService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<UVotoDTO>>> findAll() {
        List<UVotoDTO> votoDTOS = votoService.findAllVotantes();
        ResponseWrapper<List<UVotoDTO>> votos =
                new ResponseWrapper<>(true, "Todos los votos obtenidos", votoDTOS);
        return new ResponseEntity<>(votos, HttpStatus.OK);
    }

    @GetMapping("/id_voto}")
    public ResponseEntity<ResponseWrapper<UVotoDTO>> findById(@PathVariable Long id_voto) {
        UVotoDTO votoDTO = votoService.findByIdVotante(id_voto);
        ResponseWrapper<UVotoDTO> voto = new ResponseWrapper<>(true, "Voto encontrado", votoDTO);
        return new ResponseEntity<>(voto, HttpStatus.OK);
    }

    @PostMapping("/votar")
    public ResponseEntity<ResponseWrapper<UVotoDTO>> votar(
            @RequestBody UVotoCreateDTO uVotoCreateDTO) {
        UVotoDTO uVotoDTO = votoService.save(uVotoCreateDTO);
        ResponseWrapper<UVotoDTO> response =
                new ResponseWrapper<>(true, "Voto realizado sin ningun problema.", uVotoDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
