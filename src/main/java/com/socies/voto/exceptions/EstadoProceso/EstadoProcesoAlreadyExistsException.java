package com.socies.voto.exceptions.EstadoProceso;

public class EstadoProcesoAlreadyExistsException extends RuntimeException {
    public EstadoProcesoAlreadyExistsException(String message) {
        super(message);
    }
}
