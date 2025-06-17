package com.socies.voto.services;

import com.socies.voto.dtos.Provincia.ProvinciaCreateDTO;
import com.socies.voto.dtos.Provincia.ProvinciaDTO;
import com.socies.voto.dtos.Provincia.ProvinciaUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Provincia;
import com.socies.voto.repositories.ProvinciaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProvinciaService {

    @Autowired private ProvinciaRepository provinciaRepository;

    public List<ProvinciaDTO> findAll() {
        return provinciaRepository.findAll().stream()
                .map(ProvinciaDTO::new)
                .collect(Collectors.toList());
    }

    public ProvinciaDTO findById(Long id) {
        return provinciaRepository
                .findById(id)
                .map(ProvinciaDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Provincia no encontrado"));
    }

    public ProvinciaDTO create(ProvinciaCreateDTO provinciaDTO) {
        provinciaRepository
                .findByNombre(provinciaDTO.getNombre())
                .ifPresent(
                        provincia -> {
                            throw new ResourceNotFoundException("Provincia ya existente");
                        });

        Provincia provincia = new Provincia();
        provincia.setNombre(provinciaDTO.getNombre());
        provincia.setDepartamento(provinciaDTO.getDepartamento());

        provinciaRepository.save(provincia);
        return new ProvinciaDTO(provincia);
    }

    public ProvinciaDTO update(Long id, ProvinciaUpdateDTO provinciaUpdateDTO) {
        provinciaRepository
                .findByNombre(provinciaUpdateDTO.getNombre())
                .ifPresent(
                        provincia -> {
                            throw new ResourceNotFoundException("Provincia ya existente");
                        });

        Provincia provincia =
                provinciaRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Provincia no encontrado"));
        provincia.setNombre(provinciaUpdateDTO.getNombre());
        provincia.setDepartamento(provinciaUpdateDTO.getDepartamento());
        provinciaRepository.save(provincia);

        return new ProvinciaDTO(provincia);
    }
}
