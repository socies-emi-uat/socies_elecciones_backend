package com.socies.voto.controllers.administrador;

import com.socies.voto.services.PDFGenerationService;
import com.socies.voto.utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarnetSufragioController {

    @Autowired private PDFGenerationService pdfGenerationService;

    // Generación del PDF del carnet de sufragio para un votante específico
    @GetMapping("/admin/votante/pdf")
    public ResponseEntity<ResponseWrapper<byte[]>> generateVoterPDF(
            @RequestParam("votanteId") Long votanteId) {
        // Llamar al servicio para generar el PDF con los datos reales del votante
        byte[] pdfContent = pdfGenerationService.generateVoterPDF(votanteId);

        // Establecer las cabeceras de la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/pdf");
        headers.add("Content-Disposition", "attachment; filename=votante.pdf");

        // Retornar el archivo PDF generado junto con la respuesta estructurada
        ResponseWrapper<byte[]> responseWrapper =
                new ResponseWrapper<>(true, "PDF generado correctamente", pdfContent);
        return new ResponseEntity<>(responseWrapper, headers, HttpStatus.OK);
    }
}
