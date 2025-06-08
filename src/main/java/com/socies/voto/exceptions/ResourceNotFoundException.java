package com.socies.voto.exceptions;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException() {
    super("Recurso no encontrado");
  }

  // Constructor con mensaje personalizado
  public ResourceNotFoundException(String message) {
    super(message);
  }

  // Constructor con mensaje y causa (por si hay excepciones internas)
  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
