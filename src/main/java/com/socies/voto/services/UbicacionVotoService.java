package com.socies.voto.services;

import com.socies.voto.dtos.UbicacionVoto.UbicacionVotoCreateDTO;
import com.socies.voto.dtos.UbicacionVoto.UbicacionVotoDTO;
import com.socies.voto.dtos.UbicacionVoto.UbicacionVotoUpdateDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.UbicacionVoto;
import com.socies.voto.repositories.UbicacionVotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UbicacionVotoService {

    @Autowired
    private UbicacionVotoRepository ubicacionVotoRepository;

    public List<UbicacionVotoDTO> findAll() {
        return ubicacionVotoRepository.findAll()
                .stream()
                .map(UbicacionVotoDTO::new)
                .collect(Collectors.toList());
    }

    public UbicacionVotoDTO findById(Long id) {
        UbicacionVoto ubicacionVoto = ubicacionVotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación de voto no encontrada."));
        return new UbicacionVotoDTO(ubicacionVoto);
    }

    public UbicacionVotoDTO create(UbicacionVotoCreateDTO dto) {

        ubicacionVotoRepository.findByNombreUbicacion(dto.getNombreUbicacion()).ifPresent(u -> {
            throw new ResourceNotFoundException("Ubicación ya existente");
        });


        UbicacionVoto ubicacionVoto = new UbicacionVoto();
        ubicacionVoto.setNombreUbicacion(dto.getNombreUbicacion());
        ubicacionVoto.setDescripcionUbicacion(dto.getDescripcionUbicacion());
        ubicacionVoto.setLatitude(dto.getLatitude());
        ubicacionVoto.setLongitude(dto.getLongitude());
        ubicacionVoto.setDireccion(dto.getDireccion());
        ubicacionVoto.setMunicipio(dto.getMunicipio());

        ubicacionVotoRepository.save(ubicacionVoto);
        return new UbicacionVotoDTO(ubicacionVoto);
    }

    public UbicacionVotoDTO update(Long id, UbicacionVotoUpdateDTO dto) {

        UbicacionVoto ubicacionVoto = ubicacionVotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación de voto no encontrada."));

        ubicacionVoto.setNombreUbicacion(dto.getNombreUbicacion());
        ubicacionVoto.setDescripcionUbicacion(dto.getDescripcionUbicacion());
        ubicacionVoto.setLatitude(dto.getLatitude());
        ubicacionVoto.setLongitude(dto.getLongitude());
        ubicacionVoto.setDireccion(dto.getDireccion());
        ubicacionVoto.setMunicipio(dto.getMunicipio());

        ubicacionVotoRepository.save(ubicacionVoto);
        return new UbicacionVotoDTO(ubicacionVoto);
    }


}
