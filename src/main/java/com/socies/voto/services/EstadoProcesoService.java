package com.socies.voto.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socies.voto.dtos.EstadoProceso.EstadoProcesoCreateDTO;
import com.socies.voto.dtos.EstadoProceso.EstadoProcesoDTO;
import com.socies.voto.exceptions.EstadoProceso.EstadoProcesoAlreadyExistsException;
import com.socies.voto.exceptions.EstadoProceso.EstadoProcesoNotFoundException;
import com.socies.voto.models.EstadoProceso;
import com.socies.voto.repositories.EstadoProcesoRepository;

@Service
public class EstadoProcesoService {
    
    @Autowired
    private EstadoProcesoRepository estadoProcesoRepository;

    public List<EstadoProcesoDTO> obtenerTodosLosEstadosProceso() {
        return estadoProcesoRepository.findAll()
                .stream()
                .map(EstadoProcesoDTO::new)
                .collect(Collectors.toList());
    }

    public EstadoProcesoDTO obtenerEstadoProcesoPorId(Long id) {
        return estadoProcesoRepository.findById(id)
                .map(EstadoProcesoDTO::new)
                .orElseThrow(() -> new EstadoProcesoNotFoundException("El estado de proceso no fue encontrado"));
    }

    public EstadoProcesoDTO crearEstadoProceso(EstadoProcesoCreateDTO estadoProcesoCreateDTO) {
        // Verificar si ya existe por nombre
        estadoProcesoRepository.findByEstadoProceso(estadoProcesoCreateDTO.getEstadoProceso())
                .ifPresent(e -> { 
                    throw new EstadoProcesoAlreadyExistsException("El estado de proceso ya existe"); 
                });

        // Crear y guardar nuevo estado de proceso
        EstadoProceso nuevoEstadoProceso = new EstadoProceso(estadoProcesoCreateDTO.getEstadoProceso());
        EstadoProceso guardado = estadoProcesoRepository.save(nuevoEstadoProceso);
        
        // Devolver DTO del estado de proceso recién creado
        return new EstadoProcesoDTO(guardado.getId(), guardado.getEstado_proceso());
    }
}