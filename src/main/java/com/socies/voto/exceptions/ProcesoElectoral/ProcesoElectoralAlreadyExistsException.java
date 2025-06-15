package com.socies.voto.exceptions.ProcesoElectoral;

public class ProcesoElectoralAlreadyExistsException extends RuntimeException {
    public ProcesoElectoralAlreadyExistsException(String message) {
        super(message);
    }
}
