package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.Provincia.ProvinciaCreateDTO;
import com.socies.voto.dtos.Provincia.ProvinciaDTO;
import com.socies.voto.dtos.Provincia.ProvinciaUpdateDTO;
import com.socies.voto.services.ProvinciaService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administrador/provincias")
public class ProvinciaController {
    @Autowired private ProvinciaService provinciaService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<ProvinciaDTO>>> findAll() {
        List<ProvinciaDTO> provincias = provinciaService.findAll();
        ResponseWrapper<List<ProvinciaDTO>> provinciasResponse =
                new ResponseWrapper<>(true, "Todas las provincias obtenidos.", provincias);

        return new ResponseEntity<>(provinciasResponse, HttpStatus.OK);
    }

    @GetMapping("/{id_provincia}")
    public ResponseEntity<ResponseWrapper<ProvinciaDTO>> findById(@PathVariable Long id_provincia) {
        ProvinciaDTO provinciaDTO = provinciaService.findById(id_provincia);
        ResponseWrapper<ProvinciaDTO> provinciaDTOResponseWrapper =
                new ResponseWrapper<>(true, "Provincia obtenido.", provinciaDTO);
        return new ResponseEntity<>(provinciaDTOResponseWrapper, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<ProvinciaDTO>> create(
            @Valid @RequestBody ProvinciaCreateDTO provinciaDTO) {
        ProvinciaDTO provinciaDTO1 = provinciaService.create(provinciaDTO);
        ResponseWrapper<ProvinciaDTO> provinciaDTOResponseWrapper =
                new ResponseWrapper<>(true, "Provincia creado con exito.", provinciaDTO1);
        return new ResponseEntity<>(provinciaDTOResponseWrapper, HttpStatus.CREATED);
    }

    @PutMapping("/id_provincia")
    public ResponseEntity<ResponseWrapper<ProvinciaDTO>> update(
            @Valid @RequestBody ProvinciaUpdateDTO provinciaUpdateDTO,
            @PathVariable Long id_provincia) {
        ProvinciaDTO provinciaDTO = provinciaService.update(id_provincia, provinciaUpdateDTO);
        ResponseWrapper<ProvinciaDTO> provinciaDTOResponseWrapper =
                new ResponseWrapper<>(true, "Provincia creado con exito.", provinciaDTO);
        return new ResponseEntity<>(provinciaDTOResponseWrapper, HttpStatus.OK);
    }
}
