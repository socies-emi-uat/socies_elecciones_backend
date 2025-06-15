package com.socies.voto.services;

import com.socies.voto.dtos.Municipio.MunicipioCreateDTO;
import com.socies.voto.dtos.Municipio.MunicipioDTO;
import com.socies.voto.dtos.Municipio.MunicipioUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Municipio;
import com.socies.voto.repositories.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    public List<MunicipioDTO> findAll() {
        return municipioRepository.findAll().stream()
                .map(MunicipioDTO::new)
                .collect(Collectors.toList());
    }

    public MunicipioDTO findById(Long id) {
        Municipio municipio = municipioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Municipio no encontrado"));
        return new MunicipioDTO(municipio);
    }

    public MunicipioDTO create(MunicipioCreateDTO dto) {
        municipioRepository.findByNombre(dto.getNombre())
                .ifPresent(m -> {
                    throw new ResourceNotFoundException("Municipio ya existente");
                });

        Municipio municipio = new Municipio();
        municipio.setNombre(dto.getNombre());
        municipio.setProvincia(dto.getProvincia());

        municipioRepository.save(municipio);

        return new MunicipioDTO(municipio);
    }

    public MunicipioDTO update(Long id, MunicipioUpdateDTO dto) {
        municipioRepository.findByNombre(dto.getNombre())
                .ifPresent(m -> {
                    throw new ResourceNotFoundException("Municipio ya existente");
                });

        Municipio municipio = municipioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Municipio no encontrado"));

        municipio.setNombre(dto.getNombre());
        municipio.setProvincia(dto.getProvincia());

        municipioRepository.save(municipio); // importante persistir cambios

        return new MunicipioDTO(municipio);
    }
}
