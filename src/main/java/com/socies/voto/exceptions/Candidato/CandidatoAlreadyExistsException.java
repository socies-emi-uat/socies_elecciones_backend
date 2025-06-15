package com.socies.voto.exceptions.Candidato;

public class CandidatoAlreadyExistsException extends RuntimeException {
    public CandidatoAlreadyExistsException(String message) {
        super(message);
    }
}
