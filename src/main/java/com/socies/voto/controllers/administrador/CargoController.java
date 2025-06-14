package com.socies.voto.controllers.administrador;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socies.voto.dtos.Cargo.CargoCreateDTO;
import com.socies.voto.dtos.Cargo.CargoDTO;
import com.socies.voto.services.CargoService;
import com.socies.voto.utils.ResponseWrapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/administrador/cargos")
public class CargoController {

    @Autowired private final CargoService cargoService;
    // Constructor injection for CargoService
    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CargoDTO>>> listarCargos() {
        List<CargoDTO> cargos = cargoService.obtenerTodosLosCargos();
        ResponseWrapper<List<CargoDTO>> response = new ResponseWrapper<>(true, "Cargos obtenidos correctamente.", cargos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CargoDTO>> obtenerCargo(@PathVariable Long id) {
        CargoDTO cargo = cargoService.obtenerCargoPorId(id);
        ResponseWrapper<CargoDTO> response = new ResponseWrapper<>(true, "Cargo obtenido correctamente.", cargo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<CargoDTO>> crearCargo(@Valid @RequestBody CargoCreateDTO dto) {
        CargoDTO cargo = cargoService.CrearCargo(dto);
        ResponseWrapper<CargoDTO> response = new ResponseWrapper<>(true, "Cargo creado correctamente.", cargo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<CargoDTO>> actualizarCargo(@PathVariable Long id, @Valid @RequestBody CargoCreateDTO dto) {
        CargoDTO cargo = cargoService.actualizarCargo(id, dto);
        ResponseWrapper<CargoDTO> response = new ResponseWrapper<>(true, "Cargo actualizado correctamente.", cargo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> eliminarCargo(@PathVariable Long id) {
        cargoService.eliminarCargo(id);
        ResponseWrapper<Void> response = new ResponseWrapper<>(true, "Cargo eliminado correctamente.", null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/buscar")
    public ResponseEntity<ResponseWrapper<List<CargoDTO>>> buscarCargos(@RequestParam(required = false) String nombre, @RequestParam(required = false) String descripcion) {
        List<CargoDTO> cargos = cargoService.buscarCargosFiltrados(nombre, descripcion);
        ResponseWrapper<List<CargoDTO>> response = new ResponseWrapper<>(true, "Cargos obtenidos correctamente.", cargos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}