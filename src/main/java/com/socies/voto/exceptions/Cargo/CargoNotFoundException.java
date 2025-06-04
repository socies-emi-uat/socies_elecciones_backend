package com.socies.voto.exceptions.Cargo;

public class CargoNotFoundException extends RuntimeException{
    public CargoNotFoundException() {
        super("Cargo no encontrado");
    }

    // Constructor con mensaje personalizado
    public CargoNotFoundException(String message) {
        super(message);
    }

    // Constructor con mensaje y causa (por si hay excepciones internas)
    public CargoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
