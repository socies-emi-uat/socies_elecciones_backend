package com.socies.voto.services;

import com.socies.voto.dtos.Voto.AVotoDTO;
import com.socies.voto.dtos.Voto.UVotoCreateDTO;
import com.socies.voto.dtos.Voto.UVotoDTO;
import com.socies.voto.dtos.statistics.BarChartDTO;
import com.socies.voto.dtos.statistics.GeneralStatsDTO;
import com.socies.voto.dtos.statistics.LineChartDTO;
import com.socies.voto.dtos.statistics.PieChartDTO;
import com.socies.voto.dtos.usuario.UsuarioPrincipalDTO;
import com.socies.voto.exceptions.ResourceNotFoundException;
import com.socies.voto.models.MetodoVoto;
import com.socies.voto.models.UbicacionVoto;
import com.socies.voto.models.Usuario;
import com.socies.voto.models.Voto;
import com.socies.voto.repositories.MetodoVotoRepository;
import com.socies.voto.repositories.UbicacionVotoRepository;
import com.socies.voto.repositories.UsuarioRepository;
import com.socies.voto.repositories.VotoRepository;
import com.socies.voto.security.AuditLogService;
import com.socies.voto.security.DataAccessValidator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    @Autowired private VotoRepository votoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private MetodoVotoRepository metodoVotoRepository;
    @Autowired private UbicacionVotoRepository ubicacionVotoRepository;
    @Autowired private AuditLogService auditLogService;
    @Autowired private DataAccessValidator dataAccessValidator;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // separar en servicio de administracion y servicios de usuario

    // ADMINISTRADOR
    public List<AVotoDTO> findAllAdmin() {
        // Validate admin permissions
        if (!dataAccessValidator.canPerformAdminAction("LIST_ALL_VOTES", "Voto")) {
            throw new SecurityException("No tiene permisos para ver todos los votos");
        }
        
        auditLogService.logAdminAction("LIST_ALL_VOTES", "Voto", "Admin accessing all votes");
        return votoRepository.findAll().stream().map(AVotoDTO::new).collect(Collectors.toList());
    }

    // VOTANTES
    public List<UVotoDTO> findAllVotantes() {
        // Votantes can only see public vote information (without sensitive details)
        auditLogService.logDataAccess("LIST_PUBLIC_VOTES", "Voto", "ALL", "Voter accessing public vote data");
        return votoRepository.findAll().stream().map(UVotoDTO::new).collect(Collectors.toList());
    }

    // ADMINISTRADOR
    public AVotoDTO findByIdAdministrador(Long id) {
        // Validate admin permissions
        if (!dataAccessValidator.canPerformAdminAction("VIEW_VOTE_DETAILS", "Voto:" + id)) {
            throw new SecurityException("No tiene permisos para ver detalles del voto");
        }
        
        auditLogService.logAdminAction("VIEW_VOTE_DETAILS", "Voto:" + id, "Admin accessing vote details");
        return votoRepository.findById(id).map(AVotoDTO::new).orElse(null);
    }

    // VOTANTE

    public UVotoDTO findByIdVotante(Long id) {
        return votoRepository.findById(id).map(UVotoDTO::new).orElse(null);
    }

    // VOTANTE
    public UVotoDTO save(UVotoCreateDTO uVotoCreateDTO) {
        UsuarioPrincipalDTO usuarioActuador =
                (UsuarioPrincipalDTO)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Verificar si el usuario tiene permisos para votar
        if (!usuarioActuador.puedeVotar()) {
            throw new ResourceNotFoundException("El usuario no puede votar");
        }

        // Verificar si el usuario ya votó en este proceso electoral
        Long usuarioId = usuarioActuador.getId();
        Long procesoElectoralId = uVotoCreateDTO.getProcesoElectoral().getId();

        boolean yaVoto =
                votoRepository.existsByUsuarioIdAndProcesoElectoralId(
                        usuarioId, procesoElectoralId);
        if (yaVoto) {
            throw new IllegalStateException(
                    "El usuario ya ha emitido su voto en este proceso electoral.");
        }

        // Recuperar datos estáticos
        UbicacionVoto ubicacionVoto =
                ubicacionVotoRepository
                        .findById(uVotoCreateDTO.getUbicacionVoto().getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Ubicación de voto no registrada."));

        MetodoVoto metodoVoto =
                metodoVotoRepository
                        .findById(uVotoCreateDTO.getMetodoVoto().getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Método de voto no encontrado."));

        // Cargar entidad usuario completa
        Usuario usuario =
                usuarioRepository
                        .findById(usuarioId)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        // Crear voto
        Voto voto = new Voto();
        voto.setUsuario(usuario);
        voto.setMetodoVoto(metodoVoto);
        voto.setUbicacionVoto(ubicacionVoto);
        voto.setProcesoElectoral(uVotoCreateDTO.getProcesoElectoral());
        voto.setCandidatura(uVotoCreateDTO.getCandidatura());

        // Guardar voto
        votoRepository.save(voto);

        // Actualizar estado del usuario si aplica
        usuario.setEstado(true);
        usuarioRepository.save(usuario);

        return new UVotoDTO(voto);
    }

    public boolean puedeVotar() {
        UsuarioPrincipalDTO usuarioActuador =
                (UsuarioPrincipalDTO)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!usuarioActuador.puedeVotar()) {
            throw new ResourceNotFoundException("El usuario no puede votar");
        }
        return true;
    }

    // ESTADISTICIAS GENERALES
    public GeneralStatsDTO getEstadisticasGenerales(Long procesoId) {
        Long totalVotos = votoRepository.countVotosByProceso(procesoId);
        Object[] stats = votoRepository.findEstadisticasGenerales(procesoId);
        Object[] ganador = votoRepository.findCandidatoGanador(procesoId);

        return new GeneralStatsDTO(
                totalVotos,
                (Long) stats[0], // total candidatos
                (Long) stats[1], // total ubicaciones
                null, // participación - necesitarías total de electores
                (LocalDateTime) stats[2], // fecha inicio
                (LocalDateTime) stats[3], // fecha fin
                ganador != null ? (String) ganador[0] : null, // candidato ganador
                ganador != null ? (Long) ganador[1] : null // votos ganador
                );
    }

    public PieChartDTO getVotosPorCandidato(Long procesoId) {
        List<Object[]> resultados = votoRepository.findVotosPorCandidato(procesoId);
        Long totalVotos = votoRepository.countVotosByProceso(procesoId);

        List<PieChartDTO.Segment> segments =
                resultados.stream()
                        .map(
                                result -> {
                                    String nombre = (String) result[0];
                                    Long votos = (Long) result[1];
                                    Double porcentaje =
                                            (votos.doubleValue() / totalVotos.doubleValue()) * 100;
                                    return new PieChartDTO.Segment(nombre, votos, porcentaje, null);
                                })
                        .collect(Collectors.toList());

        return new PieChartDTO(
                "Distribución de Votos por Candidato",
                "Proceso Electoral ID: " + procesoId,
                "Porcentaje de votos obtenidos por cada candidato",
                segments,
                totalVotos);
    }

    public BarChartDTO getVotosPorUbicacion(Long procesoId) {
        List<Object[]> resultados = votoRepository.findVotosPorUbicacion(procesoId);

        List<BarChartDTO.BarData> bars =
                resultados.stream()
                        .map(
                                result ->
                                        new BarChartDTO.BarData(
                                                (String) result[0], // ubicación
                                                (Long) result[1], // votos
                                                null // color
                                                ))
                        .collect(Collectors.toList());

        return new BarChartDTO(
                "Votos por Ubicación",
                "Proceso Electoral ID: " + procesoId,
                "Número de votos emitidos en cada ubicación de votación",
                bars,
                "Ubicación",
                "Número de Votos",
                false);
    }

    public BarChartDTO getVotosPorMetodo(Long procesoId) {
        List<Object[]> resultados = votoRepository.findVotosPorMetodo(procesoId);

        List<BarChartDTO.BarData> bars =
                resultados.stream()
                        .map(
                                result ->
                                        new BarChartDTO.BarData(
                                                (String) result[0], // método
                                                (Long) result[1], // votos
                                                null))
                        .collect(Collectors.toList());

        return new BarChartDTO(
                "Votos por Método de Votación",
                "Proceso Electoral ID: " + procesoId,
                "Distribución de votos según el método utilizado",
                bars,
                "Método de Votación",
                "Número de Votos",
                true // horizontal
                );
    }

    public LineChartDTO getEvolucionTemporalVotos(Long procesoId) {
        List<Object[]> resultados = votoRepository.findEvolucionTemporalVotos(procesoId);

        List<LineChartDTO.DataPoint> dataPoints =
                resultados.stream()
                        .map(
                                result ->
                                        new LineChartDTO.DataPoint(
                                                result[0].toString(), // fecha
                                                (Long) result[1], // votos
                                                null // categoría
                                                ))
                        .collect(Collectors.toList());

        return new LineChartDTO(
                "Evolución Temporal de Votos",
                "Proceso Electoral ID: " + procesoId,
                "Progresión diaria del número de votos emitidos",
                dataPoints,
                "Fecha",
                "Votos Acumulados");
    }
}
