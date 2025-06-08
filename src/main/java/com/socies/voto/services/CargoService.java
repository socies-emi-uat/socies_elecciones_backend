package com.socies.voto.services;

import com.socies.voto.dtos.Cargo.CargoCreateDTO;
import com.socies.voto.dtos.Cargo.CargoDTO;
import com.socies.voto.exceptions.Cargo.CargoAlreadyExistsException;
import com.socies.voto.exceptions.Cargo.CargoNotFoundException;
import com.socies.voto.models.Cargo;
import com.socies.voto.repositories.CargoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CargoService {
  @Autowired CargoRepository cargoRepository;

  public List<CargoDTO> obtenerTodosLosCargos() {
    return cargoRepository.findAll().stream().map(CargoDTO::new).collect(Collectors.toList());
  }

  public CargoDTO obtenerCargoPorId(Long id) {
    return cargoRepository
        .findById(id)
        .map(cargo -> new CargoDTO(cargo.getId(), cargo.getNombre(), cargo.getDescripcion()))
        .orElseThrow(() -> new CargoNotFoundException("El cargo no fue encontrado"));
  }

  public CargoDTO CrearCargo(CargoCreateDTO cargoCreateDTO) {
    // ✅ Verificar si ya existe por nombre
    cargoRepository
        .findByNombre(cargoCreateDTO.getNombre_cargo())
        .ifPresent(
            c -> {
              throw new CargoAlreadyExistsException("El cargo ya existe");
            });

    // ✅ Crear y guardar nuevo cargo
    Cargo cargoNuevo = new Cargo(cargoCreateDTO.getNombre_cargo(), cargoCreateDTO.getDescripcion());
    Cargo guardado = cargoRepository.save(cargoNuevo);

    // ✅ Devolver DTO del cargo recién creado
    return new CargoDTO(guardado.getId(), guardado.getNombre(), guardado.getDescripcion());
  }

  public void eliminarCargo(Long id) {
    cargoRepository.deleteById(id);
  }
}
