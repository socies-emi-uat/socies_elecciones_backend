package com.socies.voto.exceptions.Cargo;

public class CargoAlreadyExistsException extends RuntimeException {
    public CargoAlreadyExistsException(String message) {
        super(message);
    }
}