package com.socies.voto.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socies.voto.dtos.EstadoProceso.EstadoProcesoDTO;
import com.socies.voto.repositories.EstadoProcesoRepository;

@Service
public class EstadoProcesoService {
    @Autowired
    EstadoProcesoRepository estadoProcesoRepository;
    public List<EstadoProcesoDTO> obtenerTodosLosEstadosProceso() {
        return estadoProcesoRepository.findAll().stream().map(EstadoProcesoDTO::new).collect(Collectors.toList());
    }
    public EstadoProcesoDTO obtenerEstadoProcesoPorId(Long id) {
        return estadoProcesoRepository.findById(id)
                .map(EstadoProcesoDTO::new)
                .orElseThrow(() -> new RuntimeException("El estado de proceso no fue encontrado"));
    }
    public EstadoProcesoDTO crearEstadoProceso(com.socies.voto.dtos.EstadoProceso.EstadoProcesoCreateDTO estadoProcesoCreateDTO) {
        // Verificar si ya existe por nombre
        estadoProcesoRepository.findByEstadoProceso(estadoProcesoCreateDTO.getEstadoProceso())
                .ifPresent(e -> { throw new RuntimeException("El estado de proceso ya existe"); });
        // Crear y guardar nuevo estado de proceso
        com.socies.voto.models.EstadoProceso nuevoEstadoProceso = new com.socies.voto.models.EstadoProceso(estadoProcesoCreateDTO.getEstadoProceso());
        com.socies.voto.models.EstadoProceso guardado = estadoProcesoRepository.save(nuevoEstadoProceso);
        // Devolver DTO del estado de proceso recién creado
        return new EstadoProcesoDTO(guardado.getId(), guardado.getEstado_proceso());
    }
}