package com.socies.voto.exceptions;

import com.socies.voto.dtos.AuthDTO.LoginAuthResponseDTO;
import com.socies.voto.dtos.usuario.UsuarioDTO;
import com.socies.voto.exceptions.Auth.AuthFailedException;
import com.socies.voto.exceptions.Candidato.CandidatoAlreadyExistsException;
import com.socies.voto.exceptions.Candidato.CandidatoNotFoundException;
import com.socies.voto.exceptions.Cargo.CargoAlreadyExistsException;
import com.socies.voto.exceptions.Cargo.CargoNotFoundException;
import com.socies.voto.exceptions.EstadoCandidato.EstadoCandidatoAlreadyExistsException;
import com.socies.voto.exceptions.EstadoCandidato.EstadoCandidatoNotFoundException;
import com.socies.voto.exceptions.EstadoProceso.EstadoProcesoAlreadyExistsException;
import com.socies.voto.exceptions.EstadoProceso.EstadoProcesoNotFoundException;
import com.socies.voto.exceptions.ProcesoElectoral.ProcesoElectoralAlreadyExistsException;
import com.socies.voto.exceptions.ProcesoElectoral.ProcesoElectoralNotFoundException;
import com.socies.voto.exceptions.Usuario.EmailAlreadyExistsException;
import com.socies.voto.exceptions.Usuario.UsuarioInvalidOldPasswordFoundException;
import com.socies.voto.exceptions.Usuario.UsuarioNotFoundException;
import com.socies.voto.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // usuario no encontrado
    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> handleUsuarioNotFoundException(
            UsuarioNotFoundException ex) {
        // Devolvemos un mensaje con el código 404 (Not Found)
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // email ya existe
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex) {
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthFailedException.class)
    public ResponseEntity<ResponseWrapper<LoginAuthResponseDTO>> handleAuthFailedException(
            AuthFailedException ex) {
        ResponseWrapper<LoginAuthResponseDTO> response =
                new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsuarioInvalidOldPasswordFoundException.class)
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> handleUsuarioNotFoundException(
            UsuarioInvalidOldPasswordFoundException ex) {
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CargoNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleCargoNotFound(CargoNotFoundException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CargoAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleCargoAlreadyExists(
            CargoAlreadyExistsException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EstadoProcesoNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleEstadoProcesoNotFound(
            EstadoProcesoNotFoundException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EstadoProcesoAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleEstadoProcesoAlreadyExists(
            EstadoProcesoAlreadyExistsException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EstadoCandidatoNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleEstadoCandidatoNotFound(
            EstadoCandidatoNotFoundException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EstadoCandidatoAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleEstadoCandidatoAlreadyExists(
            EstadoCandidatoAlreadyExistsException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
      
    @ExceptionHandler(ProcesoElectoralNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleProcesoElectoralAlreadyNotFound(
            ProcesoElectoralNotFoundException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProcesoElectoralAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper<Void>> handlePrcesoElectoralAlreadyExists(
            ProcesoElectoralAlreadyExistsException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
  
     @ExceptionHandler(CandidatoNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleCandidatoNotFound(
            CandidatoNotFoundException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CandidatoAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleCandidatoAlreadyExists(
            CandidatoAlreadyExistsException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    // Otros controladores de excepciones pueden ir aquí
}
