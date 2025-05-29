package com.socies.voto.exceptions;


import com.socies.voto.dtos.AuthDTO.LoginAuthResponseDTO;
import com.socies.voto.dtos.usuario.UsuarioDTO;
import com.socies.voto.exceptions.Auth.AuthFailedException;
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
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> handleUsuarioNotFoundException(UsuarioNotFoundException ex) {
        // Devolvemos un mensaje con el código 404 (Not Found)
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // email ya existe
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(AuthFailedException.class)
    public ResponseEntity<ResponseWrapper<LoginAuthResponseDTO>> handleAuthFailedException(AuthFailedException ex) {
        ResponseWrapper<LoginAuthResponseDTO> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsuarioInvalidOldPasswordFoundException.class)
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> handleUsuarioNotFoundException(UsuarioInvalidOldPasswordFoundException ex) {
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseWrapper<Void> response = new ResponseWrapper<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    // Otros controladores de excepciones pueden ir aquí
}
