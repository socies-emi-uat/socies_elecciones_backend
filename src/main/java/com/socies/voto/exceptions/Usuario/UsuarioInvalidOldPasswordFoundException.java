package com.socies.voto.exceptions.Usuario;

public class UsuarioInvalidOldPasswordFoundException extends RuntimeException {
    public UsuarioInvalidOldPasswordFoundException() {
        super("Usuario no encontrado");
    }

    // Constructor con mensaje personalizado
    public UsuarioInvalidOldPasswordFoundException(String message) {
        super(message);
    }

    // Constructor con mensaje y causa (por si hay excepciones internas)
    public UsuarioInvalidOldPasswordFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
