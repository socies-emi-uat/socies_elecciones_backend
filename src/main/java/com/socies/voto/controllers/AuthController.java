package com.socies.voto.controllers;

import com.socies.voto.dtos.AuthDTO.LoginAuthDTO;
import com.socies.voto.dtos.AuthDTO.LoginAuthResponseDTO;
import com.socies.voto.services.AuthService;
import com.socies.voto.utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

  @Autowired AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ResponseWrapper<LoginAuthResponseDTO>> login(
      @RequestBody LoginAuthDTO loginAuthDTO) {
    LoginAuthResponseDTO loginAuthResponseDTO = authService.verify(loginAuthDTO);
    ResponseWrapper<LoginAuthResponseDTO> response =
        new ResponseWrapper<>(true, "Inicio de sesion correcto.", loginAuthResponseDTO);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
