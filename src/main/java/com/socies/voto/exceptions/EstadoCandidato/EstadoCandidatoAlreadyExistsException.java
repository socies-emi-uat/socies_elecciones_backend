package com.socies.voto.exceptions.EstadoCandidato;

public class EstadoCandidatoAlreadyExistsException extends RuntimeException {
    public EstadoCandidatoAlreadyExistsException(String message) {
        super(message);
    }
}
