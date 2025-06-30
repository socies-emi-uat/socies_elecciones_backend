package com.socies.voto.dtos.Voto;

import java.time.LocalDate;

public class PDFVotoDTO {

    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String numeroCarnet;
    private String fotoUrl;
    private String numeroDocumento;

    // Getters y Setters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNumeroCarnet() {
        return numeroCarnet;
    }

    public void setNumeroCarnet(String numeroCarnet) {
        this.numeroCarnet = numeroCarnet;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
}
