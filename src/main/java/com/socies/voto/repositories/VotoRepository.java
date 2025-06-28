package com.socies.voto.repositories;

import com.socies.voto.models.Voto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByUsuarioIdAndProcesoElectoralId(Long usuarioId, Long procesoElectoralId);

    @Query(
            "SELECT c.nombreCandidatura, COUNT(v) FROM Voto v "
                    + "JOIN v.candidatura c "
                    + "WHERE v.procesoElectoral.id = :procesoId "
                    + "GROUP BY c.id, c.nombreCandidatura "
                    + "ORDER BY COUNT(v) DESC")
    List<Object[]> findVotosPorCandidato(@Param("procesoId") Long procesoId);

    // Evolución temporal de votos
    @Query(
            "SELECT DATE(v.createdAt), COUNT(v) FROM Voto v "
                    + "WHERE v.procesoElectoral.id = :procesoId "
                    + "GROUP BY DATE(v.createdAt) "
                    + "ORDER BY DATE(v.createdAt)")
    List<Object[]> findEvolucionTemporalVotos(@Param("procesoId") Long procesoId);

    // Votos por ubicación
    @Query(
            "SELECT u.nombreUbicacion, COUNT(v) FROM Voto v "
                    + "JOIN v.ubicacionVoto u "
                    + "WHERE v.procesoElectoral.id = :procesoId "
                    + "GROUP BY u.id, u.nombreUbicacion "
                    + "ORDER BY COUNT(v) DESC")
    List<Object[]> findVotosPorUbicacion(@Param("procesoId") Long procesoId);

    // Votos por método de votación
    @Query(
            "SELECT m.nombre, COUNT(v) FROM Voto v "
                    + "JOIN v.metodoVoto m "
                    + "WHERE v.procesoElectoral.id = :procesoId "
                    + "GROUP BY m.id, m.nombre "
                    + "ORDER BY COUNT(v) DESC")
    List<Object[]> findVotosPorMetodo(@Param("procesoId") Long procesoId);

    // Total de votos por proceso
    @Query("SELECT COUNT(v) FROM Voto v WHERE v.procesoElectoral.id = :procesoId")
    Long countVotosByProceso(@Param("procesoId") Long procesoId);

    // Estadísticas generales
    @Query(
            "SELECT "
                    + "COUNT(DISTINCT v.candidatura), "
                    + "COUNT(DISTINCT v.ubicacionVoto), "
                    + "MIN(v.createdAt), "
                    + "MAX(v.createdAt) "
                    + "FROM Voto v WHERE v.procesoElectoral.id = :procesoId")
    Object[] findEstadisticasGenerales(@Param("procesoId") Long procesoId);

    // Candidato ganador
    @Query(
            "SELECT c.nombreCandidatura, COUNT(v) FROM Voto v "
                    + "JOIN v.candidatura c "
                    + "WHERE v.procesoElectoral.id = :procesoId "
                    + "GROUP BY c.id, c.nombreCandidatura "
                    + "ORDER BY COUNT(v) DESC "
                    + "LIMIT 1")
    Object[] findCandidatoGanador(@Param("procesoId") Long procesoId);
}
