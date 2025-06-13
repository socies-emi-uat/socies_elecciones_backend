package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.Cargo.CargoCreateDTO;
import com.socies.voto.dtos.Cargo.CargoDTO;
import com.socies.voto.services.CargoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cargos")
public class CargoController {

    @Autowired private CargoService cargoService;

    @GetMapping
    public ResponseEntity<List<CargoDTO>> listarCargos() {
        return ResponseEntity.ok(cargoService.obtenerTodosLosCargos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargoDTO> obtenerCargo(@PathVariable Long id) {
        return ResponseEntity.ok(cargoService.obtenerCargoPorId(id));
    }

    @PostMapping
    public ResponseEntity<CargoDTO> crearCargo(@Valid @RequestBody CargoCreateDTO dto) {
        return ResponseEntity.ok(cargoService.CrearCargo(dto));
    }

    // Faltaría implementar en el Service
    @PutMapping("/{id}")
    public ResponseEntity<CargoDTO> actualizarCargo(
            @PathVariable Long id, @Valid @RequestBody CargoCreateDTO dto) {
        return ResponseEntity.ok(cargoService.actualizarCargo(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCargo(@PathVariable Long id) {
        cargoService.eliminarCargo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CargoDTO>> buscarCargos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion) {
        return ResponseEntity.ok(cargoService.buscarCargosFiltrados(nombre, descripcion));
    }
}
