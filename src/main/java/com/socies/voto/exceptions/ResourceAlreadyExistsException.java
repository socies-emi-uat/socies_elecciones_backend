package com.socies.voto.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException() {
        super("Recurso ya existe");
    }

    // Constructor con mensaje personalizado
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    // Constructor con mensaje y causa (por si hay excepciones internas)
    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
