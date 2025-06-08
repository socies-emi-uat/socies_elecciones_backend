package com.socies.voto.exceptions.Rol;

public class RolNotFoundException extends RuntimeException {
  public RolNotFoundException() {
    super("Rol no encontrado");
  }

  // Constructor con mensaje personalizado
  public RolNotFoundException(String message) {
    super(message);
  }

  // Constructor con mensaje y causa (por si hay excepciones internas)
  public RolNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
