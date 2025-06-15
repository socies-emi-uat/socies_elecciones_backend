package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.Departamento.DepartamentoCreateDTO;
import com.socies.voto.dtos.Departamento.DepartamentoDTO;
import com.socies.voto.dtos.Departamento.DepartamentoUpdateDTO;
import com.socies.voto.services.DepartamentoService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administrador/departamentos")
public class DepartamentoController {
    @Autowired private DepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<DepartamentoDTO>>> findAll() {
        List<DepartamentoDTO> departamentosDTO = departamentoService.findAll();
        ResponseWrapper<List<DepartamentoDTO>> departamentos =
                new ResponseWrapper<>(true, "Todos los departamentos.", departamentosDTO);
        return new ResponseEntity<>(departamentos, HttpStatus.OK);
    }

    @GetMapping("/{id_departamento}")
    public ResponseEntity<ResponseWrapper<DepartamentoDTO>> findById(@PathVariable Long id) {
        DepartamentoDTO departamentoDTO = departamentoService.findById(id);
        ResponseWrapper<DepartamentoDTO> departamento =
                new ResponseWrapper<>(true, "Departamento encontrado.", departamentoDTO);
        return new ResponseEntity<>(departamento, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<DepartamentoDTO>> crearDepartamento(
            @Valid @RequestBody DepartamentoCreateDTO departamentoCreateDTO) {
        DepartamentoDTO departamentoDTO = departamentoService.create(departamentoCreateDTO);
        ResponseWrapper<DepartamentoDTO> departamento =
                new ResponseWrapper<>(true, "Departamento encontrado.", departamentoDTO);
        return new ResponseEntity<>(departamento, HttpStatus.CREATED);
    }

    @PutMapping("/{id_departamento}")
    public ResponseEntity<ResponseWrapper<DepartamentoDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DepartamentoUpdateDTO departamentoUpdateDTO) {
        DepartamentoDTO departamentoDTO = departamentoService.update(departamentoUpdateDTO, id);
        ResponseWrapper<DepartamentoDTO> departamento =
                new ResponseWrapper<>(true, "Departamento encontrado.", departamentoDTO);

        return new ResponseEntity<>(departamento, HttpStatus.OK);
    }
}
