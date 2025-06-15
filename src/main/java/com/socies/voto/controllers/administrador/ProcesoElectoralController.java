package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralCreateDTO;
import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralDTO;
import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralUpdateDTO;
import com.socies.voto.repositories.ProcesoElectoralRepository;
import com.socies.voto.services.ProcesoElectoralService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administrador/proceso-electoral")
public class ProcesoElectoralController {
    @Autowired private ProcesoElectoralService procesoElectoralService;
    @Autowired private ProcesoElectoralRepository procesoElectoralRepository;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<ProcesoElectoralDTO>>> getAll() {
        List<ProcesoElectoralDTO> procesoElectoralDTOS = procesoElectoralService.findAll();
        ResponseWrapper<List<ProcesoElectoralDTO>> procesos =
                new ResponseWrapper<>(
                        true, "Lista de todos los procesos electorales.", procesoElectoralDTOS);

        return new ResponseEntity<>(procesos, HttpStatus.OK);
    }

    @GetMapping("/{id_proceso}")
    public ResponseEntity<ResponseWrapper<ProcesoElectoralDTO>> getById(@PathVariable Long id) {
        ProcesoElectoralDTO procesoElectoralDTO = procesoElectoralService.findById(id);
        ResponseWrapper<ProcesoElectoralDTO> procesos =
                new ResponseWrapper<>(true, "Prceso electoral obtenido.", procesoElectoralDTO);

        return new ResponseEntity<>(procesos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<ProcesoElectoralDTO>> crearProcesoElectoral(
            @Valid @RequestBody ProcesoElectoralCreateDTO procesoElectoralDTO) {
        ProcesoElectoralDTO proceso = procesoElectoralService.create(procesoElectoralDTO);
        ResponseWrapper<ProcesoElectoralDTO> nuevo_proceso =
                new ResponseWrapper<>(true, "Proceso electoral creada.", proceso);

        return new ResponseEntity<>(nuevo_proceso, HttpStatus.CREATED);
    }

    @PutMapping("/{id_prceso}")
    public ResponseEntity<ResponseWrapper<ProcesoElectoralDTO>> actualizarProcesoElectoral(
            @Valid @RequestBody ProcesoElectoralUpdateDTO procesoElectoralDTO,
            @PathVariable Long id) {
        ProcesoElectoralDTO proceso = procesoElectoralService.actualizar(procesoElectoralDTO, id);
        ResponseWrapper<ProcesoElectoralDTO> proceso_actualizado =
                new ResponseWrapper<>(true, "Proceso electoral creada.", proceso);

        return new ResponseEntity<>(proceso_actualizado, HttpStatus.OK);
    }
}
