package com.socies.voto.services;

import com.socies.voto.dtos.Candidato.CandidatoCreateDTO;
import com.socies.voto.dtos.Candidato.CandidatoDTO;
import com.socies.voto.dtos.Candidato.CandidatoUpdateDTO;
import com.socies.voto.exceptions.Candidato.CandidatoAlreadyExistsException;
import com.socies.voto.exceptions.Candidato.CandidatoNotFoundException;
import com.socies.voto.models.Candidato;
import com.socies.voto.models.Cargo;
import com.socies.voto.models.EstadoCandidato;
import com.socies.voto.repositories.CandidatoRepository;
import com.socies.voto.repositories.CargoRepository;
import com.socies.voto.repositories.EstadoCandidatoRepository;
import com.socies.voto.security.AuditLogService;
import com.socies.voto.security.DataAccessValidator;
import com.socies.voto.security.InputSanitizer;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidatoService {

    @Autowired private CandidatoRepository candidatoRepository;
    @Autowired private EstadoCandidatoRepository estadoCandidatoRepository;
    @Autowired private CargoRepository cargoRepository;
    @Autowired private InputSanitizer inputSanitizer;
    @Autowired private AuditLogService auditLogService;
    @Autowired private DataAccessValidator dataAccessValidator;

    public List<CandidatoDTO> obtenerTodosLosCandidatos() {
        // Log data access for audit
        auditLogService.logDataAccess("LIST_CANDIDATES", "Candidato", "ALL", 
            "Retrieving all candidates list");
        
        return candidatoRepository.findAll().stream()
                .map(CandidatoDTO::new)
                .collect(Collectors.toList());
    }

    public CandidatoDTO obtenerCandidatoPorId(Long id) {
        // Log data access for audit
        auditLogService.logDataAccess("GET_CANDIDATE", "Candidato", id.toString(), 
            "Retrieving candidate by ID");
        
        return candidatoRepository
                .findById(id)
                .map(CandidatoDTO::new)
                .orElseThrow(
                        () -> new CandidatoNotFoundException("El candidato no fue encontrado"));
    }

    public CandidatoDTO crearCandidato(CandidatoCreateDTO candidatoCreateDTO) {
        // Validate admin permissions
        if (!dataAccessValidator.canPerformAdminAction("CREATE_CANDIDATE", "Candidato")) {
            throw new SecurityException("No tiene permisos para crear candidatos");
        }

        // Sanitize and validate input data
        String nombreCandidato = inputSanitizer.validateAndSanitizeText(
            candidatoCreateDTO.getNombreCandidato(), "Nombre del candidato");
        String apPaterno = inputSanitizer.validateAndSanitizeText(
            candidatoCreateDTO.getApPaterno(), "Apellido paterno");
        String apMaterno = inputSanitizer.validateAndSanitizeText(
            candidatoCreateDTO.getApMaterno(), "Apellido materno");
        String ciCandidato = inputSanitizer.validateAndSanitizeCI(
            candidatoCreateDTO.getCiCandidato(), "Cédula de identidad");
        String correoCandidato = inputSanitizer.validateAndSanitizeEmail(
            candidatoCreateDTO.getCorreoCandidato(), "Correo electrónico");
        String propuesta = inputSanitizer.validateAndSanitizeText(
            candidatoCreateDTO.getPropuesta(), "Propuesta");

        // Verificar si la propuesta es requerida
        if (propuesta == null || propuesta.isEmpty()) {
            throw new RuntimeException("La propuesta es requerida");
        }
        
        // Verificar si ya existe por CI
        candidatoRepository
                .findByCedulaIdentidad(ciCandidato)
                .ifPresent(
                        c -> {
                            auditLogService.logSecurityViolation("DUPLICATE_CANDIDATE_CI", 
                                "Attempt to create candidate with existing CI: " + inputSanitizer.maskSensitiveData(ciCandidato));
                            throw new CandidatoAlreadyExistsException("El candidato ya existe");
                        });
        
        // Verificar si el correo ya existe
        if (correoCandidato != null && !correoCandidato.isEmpty()) {
            candidatoRepository
                    .findByCorreo(correoCandidato)
                    .ifPresent(
                            c -> {
                                auditLogService.logSecurityViolation("DUPLICATE_CANDIDATE_EMAIL", 
                                    "Attempt to create candidate with existing email: " + inputSanitizer.maskSensitiveData(correoCandidato));
                                throw new CandidatoAlreadyExistsException(
                                        "El correo electrónico ya está registrado");
                            });
        }

        EstadoCandidato estadoCandidato =
                estadoCandidatoRepository
                        .findById(candidatoCreateDTO.getEstadoCandidatoId())
                        .orElseThrow(
                                () -> new RuntimeException("Estado de candidato no encontrado"));
        Cargo cargo =
                cargoRepository
                        .findById(candidatoCreateDTO.getCargoId())
                        .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        // Crear y guardar nuevo candidato con datos sanitizados
        Candidato nuevoCandidato = new Candidato();
        nuevoCandidato.setNombreCandidato(nombreCandidato);
        nuevoCandidato.setApellidoPaterno(apPaterno);
        nuevoCandidato.setApellidoMaterno(apMaterno);
        nuevoCandidato.setCedulaIdentidad(ciCandidato);
        nuevoCandidato.setFechaNacimiento(candidatoCreateDTO.getFechaNacimiento());
        nuevoCandidato.setFotoUrl(candidatoCreateDTO.getFotoUrl());
        nuevoCandidato.setCorreo(correoCandidato);
        nuevoCandidato.setPropuesta(propuesta);
        nuevoCandidato.setEstadoCandidato(estadoCandidato);
        nuevoCandidato.setCargo(cargo);
        
        Candidato guardado = candidatoRepository.save(nuevoCandidato);
        
        // Log successful candidate creation
        auditLogService.logAdminAction("CREATE_CANDIDATE", "Candidato:" + guardado.getId(),
            "Candidate created: " + nombreCandidato + " " + apPaterno);
        
        return new CandidatoDTO(guardado);
    }

    public CandidatoDTO actualizarCandidato(Long id, CandidatoUpdateDTO candidatoUpdateDTO) {
        Candidato candidato =
                candidatoRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new CandidatoNotFoundException("Candidato no encontrado"));
        // Verificar si la propuesta es requerida
        if (candidatoUpdateDTO.getPropuesta() == null
                || candidatoUpdateDTO.getPropuesta().isEmpty()) {
            throw new RuntimeException("La propuesta es requerida");
        }
        // Verificar si el CI ya existe (excepto para el mismo registro)
        candidatoRepository
                .findByCedulaIdentidad(candidatoUpdateDTO.getCiCandidato())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(
                        c -> {
                            throw new CandidatoAlreadyExistsException("El CI ya está registrado");
                        });
        // Verificar si el correo ya existe (excepto para el mismo registro)
        if (candidatoUpdateDTO.getCorreoCandidato() != null
                && !candidatoUpdateDTO.getCorreoCandidato().isEmpty()) {
            candidatoRepository
                    .findByCorreo(candidatoUpdateDTO.getCorreoCandidato())
                    .filter(c -> !c.getId().equals(id))
                    .ifPresent(
                            c -> {
                                throw new CandidatoAlreadyExistsException(
                                        "El correo electrónico ya está registrado");
                            });
        }

        EstadoCandidato estadoCandidato =
                estadoCandidatoRepository
                        .findById(candidatoUpdateDTO.getEstadoCandidatoId())
                        .orElseThrow(
                                () -> new RuntimeException("Estado de candidato no encontrado"));
        Cargo cargo =
                cargoRepository
                        .findById(candidatoUpdateDTO.getCargoId())
                        .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        candidato.setNombreCandidato(candidatoUpdateDTO.getNombreCandidato());
        candidato.setApellidoPaterno(candidatoUpdateDTO.getApPaterno());
        candidato.setApellidoMaterno(candidatoUpdateDTO.getApMaterno());
        candidato.setCedulaIdentidad(candidatoUpdateDTO.getCiCandidato());
        candidato.setFechaNacimiento(candidatoUpdateDTO.getFechaNacimiento());
        candidato.setFotoUrl(candidatoUpdateDTO.getFotoUrl());
        candidato.setCorreo(candidatoUpdateDTO.getCorreoCandidato());
        candidato.setPropuesta(candidatoUpdateDTO.getPropuesta());
        candidato.setEstadoCandidato(estadoCandidato);
        candidato.setCargo(cargo);

        Candidato actualizado = candidatoRepository.save(candidato);
        return new CandidatoDTO(actualizado);
    }

    public void eliminarCandidato(Long id) {
        if (!candidatoRepository.existsById(id)) {
            throw new CandidatoNotFoundException("Candidato no encontrado con id " + id);
        }
        candidatoRepository.deleteById(id);
    }
}
