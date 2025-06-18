package com.socies.voto.exceptions.Candidatura;

public class CandidaturaAlreadyExistsException extends RuntimeException {
    public CandidaturaAlreadyExistsException(String message) {
        super(message);
    }
}
