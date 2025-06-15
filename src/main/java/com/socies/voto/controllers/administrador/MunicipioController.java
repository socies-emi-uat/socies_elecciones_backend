package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.Municipio.MunicipioCreateDTO;
import com.socies.voto.dtos.Municipio.MunicipioDTO;
import com.socies.voto.dtos.Municipio.MunicipioUpdateDTO;
import com.socies.voto.services.MunicipioService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrador/municipios")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<MunicipioDTO>>> findAll() {
        List<MunicipioDTO> municipios = municipioService.findAll();
        ResponseWrapper<List<MunicipioDTO>> response =
                new ResponseWrapper<>(true, "Todos los municipios obtenidos.", municipios);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id_municipio}")
    public ResponseEntity<ResponseWrapper<MunicipioDTO>> findById(@PathVariable Long id_municipio) {
        MunicipioDTO municipioDTO = municipioService.findById(id_municipio);
        ResponseWrapper<MunicipioDTO> response =
                new ResponseWrapper<>(true, "Municipio obtenido.", municipioDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<MunicipioDTO>> create(
            @Valid @RequestBody MunicipioCreateDTO municipioDTO) {
        MunicipioDTO createdMunicipio = municipioService.create(municipioDTO);
        ResponseWrapper<MunicipioDTO> response =
                new ResponseWrapper<>(true, "Municipio creado con éxito.", createdMunicipio);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id_municipio}")
    public ResponseEntity<ResponseWrapper<MunicipioDTO>> update(
            @Valid @RequestBody MunicipioUpdateDTO municipioUpdateDTO,
            @PathVariable Long id_municipio) {
        MunicipioDTO updatedMunicipio = municipioService.update(id_municipio, municipioUpdateDTO);
        ResponseWrapper<MunicipioDTO> response =
                new ResponseWrapper<>(true, "Municipio actualizado con éxito.", updatedMunicipio);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
