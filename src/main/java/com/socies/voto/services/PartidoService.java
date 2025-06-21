package com.socies.voto.services;

import com.socies.voto.dtos.Partido.PartidoCreateDTO;
import com.socies.voto.dtos.Partido.PartidoDTO;
import com.socies.voto.dtos.Partido.PartidoUpdateDTO;
import com.socies.voto.exceptions.ResourceAlreadyExistsException;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.Partido;
import com.socies.voto.repositories.PartidoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartidoService {
    @Autowired private PartidoRepository partidoRepository;

    public List<PartidoDTO> findAll() {
        return partidoRepository.findAll().stream()
                .map(PartidoDTO::new)
                .collect(Collectors.toList());
    }

    public PartidoDTO findById(Long id) {
        return partidoRepository
                .findById(id)
                .map(PartidoDTO::new)
                .orElseThrow(
                        () -> new ResourceNotFoundException("El partido no ah sido encontrado"));
    }

    public PartidoDTO create(PartidoCreateDTO dto) {
        partidoRepository
                .findByNombrePartido(dto.getNombrePartido())
                .ifPresent(
                        p -> {
                            throw new ResourceAlreadyExistsException(
                                    "El nombre del partido ya está registrado.");
                        });

        partidoRepository
                .findBySigla(dto.getSigla())
                .ifPresent(
                        p -> {
                            throw new ResourceAlreadyExistsException(
                                    "La sigla ya está registrada.");
                        });

        Partido partido = new Partido();

        partido.setNombrePartido(dto.getNombrePartido().trim());
        partido.setSigla(dto.getSigla().trim());
        partido.setLema(dto.getLema() != null ? dto.getLema().trim() : null);
        partido.setLogoUrl(dto.getLogoUrl() != null ? dto.getLogoUrl().trim() : null);
        partido.setColorHex(dto.getColorHex() != null ? dto.getColorHex().trim() : null);
        partido.setRepresentanteLegal(
                dto.getRepresentanteLegal() != null ? dto.getRepresentanteLegal().trim() : null);
        partido.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion().trim() : null);
        partido.setDireccionSede(
                dto.getDireccionSede() != null ? dto.getDireccionSede().trim() : null);
        partido.setPaginaWeb(dto.getPaginaWeb() != null ? dto.getPaginaWeb().trim() : null);
        partido.setTelefonoContacto(
                dto.getTelefonoContacto() != null ? dto.getTelefonoContacto().trim() : null);
        partido.setCorreoContacto(
                dto.getCorreoContacto() != null ? dto.getCorreoContacto().trim() : null);
        partido.setFechaFundacion(dto.getFechaFundacion() != null ? dto.getFechaFundacion() : null);

        partido.setEstado(true);
        partido.setPais("Bolivia");

        partidoRepository.save(partido);
        return new PartidoDTO(partido);
    }

    public PartidoDTO update(Long id, PartidoUpdateDTO dto) {
        Partido partido =
                partidoRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("El partido no existe."));

        partidoRepository
                .findByNombrePartido(dto.getNombrePartido())
                .ifPresent(
                        p -> {
                            if (!p.getId().equals(id)) {
                                throw new ResourceAlreadyExistsException(
                                        "El nombre del partido ya está registrado.");
                            }
                        });

        partidoRepository
                .findBySigla(dto.getSigla())
                .ifPresent(
                        p -> {
                            if (!p.getId().equals(id)) {
                                throw new ResourceAlreadyExistsException(
                                        "La sigla ya está registrada.");
                            }
                        });

        partido.setNombrePartido(dto.getNombrePartido().trim());
        partido.setSigla(dto.getSigla().trim());
        partido.setLema(dto.getLema() != null ? dto.getLema().trim() : null);
        partido.setLogoUrl(dto.getLogoUrl() != null ? dto.getLogoUrl().trim() : null);
        partido.setColorHex(dto.getColorHex() != null ? dto.getColorHex().trim() : null);
        partido.setRepresentanteLegal(
                dto.getRepresentanteLegal() != null ? dto.getRepresentanteLegal().trim() : null);
        partido.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion().trim() : null);
        partido.setDireccionSede(
                dto.getDireccionSede() != null ? dto.getDireccionSede().trim() : null);
        partido.setPaginaWeb(dto.getPaginaWeb() != null ? dto.getPaginaWeb().trim() : null);
        partido.setTelefonoContacto(
                dto.getTelefonoContacto() != null ? dto.getTelefonoContacto().trim() : null);
        partido.setCorreoContacto(
                dto.getCorreoContacto() != null ? dto.getCorreoContacto().trim() : null);
        partido.setFechaFundacion(dto.getFechaFundacion() != null ? dto.getFechaFundacion() : null);

        partidoRepository.save(partido);
        return new PartidoDTO(partido);
    }

    public String disableOenable(Long id) {
        String mensaje = "El partido fue desabilitado.";
        Partido partido =
                partidoRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("El partido no existe."));

        if (partido.isEstado()) {
            partido.setEstado(false);
        } else {
            partido.setEstado(true);
            mensaje = "El partido fue habilitado";
        }

        partidoRepository.save(partido);

        return mensaje;
    }
}
