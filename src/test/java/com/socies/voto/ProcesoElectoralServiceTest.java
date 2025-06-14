package com.socies.voto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralCreateDTO;
import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralDTO;
import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralUpdateDTO;
import com.socies.voto.exceptions.ProcesoElectoral.ProcesoElectoralAlreadyExistsException;
import com.socies.voto.exceptions.ProcesoElectoral.ProcesoElectoralNotFoundException;
import com.socies.voto.models.EstadoProceso;
import com.socies.voto.models.ProcesoElectoral;
import com.socies.voto.repositories.ProcesoElectoralRepository;
import com.socies.voto.services.ProcesoElectoralService;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ProcesoElectoralServiceTest {

    @InjectMocks private ProcesoElectoralService service;

    @Mock private ProcesoElectoralRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_whenProcesoDoesNotExist_createsSuccessfully() {
        // Arrange
        EstadoProceso estado = new EstadoProceso();
        estado.setEstadoProceso("Activo");

        ProcesoElectoralCreateDTO dto = new ProcesoElectoralCreateDTO();
        dto.setNombreProceso("Eleccion 2025");
        dto.setDescripcionProceso("Eleccion general");
        dto.setFechaInicio(LocalDateTime.now());
        dto.setFechaFin(LocalDateTime.now().plusDays(30));
        dto.setEstadoProceso(estado);

        when(repository.findByNombreProceso(dto.getNombreProceso())).thenReturn(Optional.empty());

        // Act
        ProcesoElectoralDTO result = service.create(dto);

        // Assert
        assertEquals(dto.getNombreProceso(), result.getNombreProceso());
        verify(repository, times(1)).save(any(ProcesoElectoral.class));
    }

    @Test
    void testCreate_whenProcesoExists_throwsException() {
        // Arrange
        ProcesoElectoralCreateDTO dto = new ProcesoElectoralCreateDTO();
        dto.setNombreProceso("Eleccion 2025");

        when(repository.findByNombreProceso(dto.getNombreProceso()))
                .thenReturn(Optional.of(new ProcesoElectoral()));

        // Act & Assert
        assertThrows(ProcesoElectoralAlreadyExistsException.class, () -> service.create(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void testFindById_whenFound_returnsDTO() {
        ProcesoElectoral proceso = new ProcesoElectoral();
        proceso.setNombreProceso("Eleccion");
        proceso.setDescripcionProceso("Desc");
        proceso.setFechaInicio(LocalDateTime.now());
        proceso.setFechaFin(LocalDateTime.now().plusDays(1));

        when(repository.findById(1L)).thenReturn(Optional.of(proceso));

        ProcesoElectoralDTO dto = service.findById(1L);

        assertEquals("Eleccion", dto.getNombreProceso());
    }

    @Test
    void testFindById_whenNotFound_throwsException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProcesoElectoralNotFoundException.class, () -> service.findById(999L));
    }

    @Test
    void testFindAll_returnsListOfDTOs() {
        ProcesoElectoral p1 = new ProcesoElectoral();
        p1.setNombreProceso("Proceso 1");
        p1.setDescripcionProceso("Descripcion 1");
        p1.setFechaInicio(LocalDateTime.now());
        p1.setFechaFin(LocalDateTime.now().plusDays(10));

        ProcesoElectoral p2 = new ProcesoElectoral();
        p2.setNombreProceso("Proceso 2");
        p2.setDescripcionProceso("Descripcion 2");
        p2.setFechaInicio(LocalDateTime.now());
        p2.setFechaFin(LocalDateTime.now().plusDays(20));

        when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<ProcesoElectoralDTO> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("Proceso 1", result.get(0).getNombreProceso());
    }

    @Test
    void testActualizar_whenValid_updatesFields() {
        EstadoProceso estado = new EstadoProceso();
        estado.setEstadoProceso("En curso");

        ProcesoElectoral proceso = new ProcesoElectoral();
        proceso.setNombreProceso("Original");
        proceso.setDescripcionProceso("Original desc");
        proceso.setFechaInicio(LocalDateTime.now());
        proceso.setFechaFin(LocalDateTime.now().plusDays(10));
        proceso.setEstadoProceso(estado);

        ProcesoElectoralUpdateDTO update = new ProcesoElectoralUpdateDTO();
        update.setNombreProceso("Nuevo nombre");
        update.setDescripcionProceso("Nueva descripcion");

        when(repository.findById(1L)).thenReturn(Optional.of(proceso));

        ProcesoElectoralDTO updated = service.actualizar(update, 1L);

        assertEquals("Nuevo nombre", updated.getNombreProceso());
        assertEquals("Nueva descripcion", updated.getDescripcionProceso());
    }
}
