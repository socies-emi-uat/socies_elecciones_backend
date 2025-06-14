package com.socies.voto.exceptions.ProcesoElectoral;

public class ProcesoElectoralNotFoundException extends RuntimeException {
    public ProcesoElectoralNotFoundException() {
        super("Usuario no encontrado");
    }

    // Constructor con mensaje personalizado
    public ProcesoElectoralNotFoundException(String message) {
        super(message);
    }

    // Constructor con mensaje y causa (por si hay excepciones internas)
    public ProcesoElectoralNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
