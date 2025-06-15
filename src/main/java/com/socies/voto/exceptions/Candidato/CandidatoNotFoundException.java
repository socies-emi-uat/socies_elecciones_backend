package com.socies.voto.exceptions.Candidato;

public class CandidatoNotFoundException extends RuntimeException {
    public CandidatoNotFoundException() {
        super("Candidato no encontrado");
    }

    // Constructor con mensaje personalizado
    public CandidatoNotFoundException(String message) {
        super(message);
    }

    // Constructor con mensaje y causa (por si hay excepciones internas)
    public CandidatoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
