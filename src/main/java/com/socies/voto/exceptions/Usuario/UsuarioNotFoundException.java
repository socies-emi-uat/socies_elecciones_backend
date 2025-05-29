package com.socies.voto.exceptions.Usuario;

public class UsuarioNotFoundException extends RuntimeException{
    public UsuarioNotFoundException() {
        super("Usuario no encontrado");
    }

    // Constructor con mensaje personalizado
    public UsuarioNotFoundException(String message) {
        super(message);
    }

    // Constructor con mensaje y causa (por si hay excepciones internas)
    public UsuarioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
