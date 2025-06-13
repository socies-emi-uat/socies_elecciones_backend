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
                .map(
                        cargo ->
                                new CargoDTO(
                                        cargo.getId(), cargo.getNombre(), cargo.getDescripcion()))
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
        Cargo cargoNuevo =
                new Cargo(cargoCreateDTO.getNombre_cargo(), cargoCreateDTO.getDescripcion());
        Cargo guardado = cargoRepository.save(cargoNuevo);

        // ✅ Devolver DTO del cargo recién creado
        return new CargoDTO(guardado.getId(), guardado.getNombre(), guardado.getDescripcion());
    }

    public void eliminarCargo(Long id) {
        cargoRepository.deleteById(id);
    }

    public CargoDTO actualizarCargo(Long id, CargoCreateDTO dto) {
        Cargo cargo =
                cargoRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new CargoNotFoundException(
                                                "Cargo no encontrado para actualizar"));

        // Verificar si el nuevo nombre ya existe (y no es el mismo cargo)
        cargoRepository
                .findByNombre(dto.getNombre_cargo())
                .ifPresent(
                        c -> {
                            if (!c.getId().equals(id)) {
                                throw new CargoAlreadyExistsException(
                                        "Ya existe otro cargo con ese nombre");
                            }
                        });

        cargo.setNombre(dto.getNombre_cargo());
        cargo.setDescripcion(dto.getDescripcion());

        Cargo actualizado = cargoRepository.save(cargo);
        return new CargoDTO(actualizado);
    }

    public List<CargoDTO> buscarCargosFiltrados(String nombre, String descripcion) {
        if (nombre != null && descripcion != null) {
            return cargoRepository
                    .findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(
                            nombre, descripcion)
                    .stream()
                    .map(CargoDTO::new)
                    .collect(Collectors.toList());
        } else if (nombre != null) {
            return cargoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                    .map(CargoDTO::new)
                    .collect(Collectors.toList());
        } else if (descripcion != null) {
            return cargoRepository.findByDescripcionContainingIgnoreCase(descripcion).stream()
                    .map(CargoDTO::new)
                    .collect(Collectors.toList());
        }
        return obtenerTodosLosCargos(); // Si no hay filtros
    }
}
