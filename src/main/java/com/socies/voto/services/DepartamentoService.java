package com.socies.voto.services;

import com.socies.voto.dtos.Departamento.DepartamentoCreateDTO;
import com.socies.voto.dtos.Departamento.DepartamentoDTO;
import com.socies.voto.dtos.Departamento.DepartamentoUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Departamento;
import com.socies.voto.repositories.DepartamentoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService {
    @Autowired private DepartamentoRepository departamentoRepository;

    public List<DepartamentoDTO> findAll() {
        return departamentoRepository.findAll().stream()
                .map(DepartamentoDTO::new)
                .collect(Collectors.toList());
    }

    public DepartamentoDTO findById(Long id) {
        Departamento departamento =
                departamentoRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Departamento no encontrado."));
        return new DepartamentoDTO(departamento);
    }

    public DepartamentoDTO create(DepartamentoCreateDTO departamentoCreateDTO) {
        departamentoRepository
                .findByNombre(departamentoCreateDTO.getNombre())
                .ifPresent(
                        departamento -> {
                            throw new ResourceNotFoundException("Departamento ya existe");
                        });

        Departamento departamento = new Departamento();
        departamento.setNombre(departamentoCreateDTO.getNombre());

        departamentoRepository.save(departamento);
        return new DepartamentoDTO(departamento);
    }

    public DepartamentoDTO update(DepartamentoUpdateDTO departamentoUpdateDTO, Long id) {
        Departamento departamento =
                departamentoRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Departamento no encontrado."));
        departamentoRepository
                .findByNombre(departamentoUpdateDTO.getNombre())
                .ifPresent(
                        d -> {
                            throw new ResourceNotFoundException(
                                    "El departamento ya existe " + d.getNombre());
                        });

        departamento.setNombre(departamentoUpdateDTO.getNombre());
        departamentoRepository.save(departamento);

        return new DepartamentoDTO(departamento);
    }
}
