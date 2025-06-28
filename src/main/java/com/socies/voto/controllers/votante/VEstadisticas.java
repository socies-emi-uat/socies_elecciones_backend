package com.socies.voto.controllers.votante;

import com.socies.voto.dtos.statistics.GeneralStatsDTO;
import com.socies.voto.services.VotoService;
import com.socies.voto.utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votante/estadisticas")
public class VEstadisticas {
    @Autowired VotoService votoService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<GeneralStatsDTO>> generarEstadisticas() {
        GeneralStatsDTO estadistica = new GeneralStatsDTO();
        ResponseWrapper<GeneralStatsDTO> responseWrapper =
                new ResponseWrapper<>(true, "Estadisticas generales obtenidas.", estadistica);
        return new ResponseEntity<>(responseWrapper, HttpStatus.OK);
    }
}
