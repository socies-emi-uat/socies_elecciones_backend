package com.socies.voto.services;

import com.socies.voto.dtos.AuthDTO.LoginAuthDTO;
import com.socies.voto.dtos.AuthDTO.LoginAuthResponseDTO;
import com.socies.voto.dtos.usuario.UsuarioPrincipalDTO;
import com.socies.voto.exceptions.Auth.AuthFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    public LoginAuthResponseDTO verify(LoginAuthDTO loginAuthDTO) {
        Authentication authentication;
        try {
            authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginAuthDTO.getEmail(), loginAuthDTO.getPassword()));
        } catch (BadCredentialsException es) {
            throw new AuthFailedException("Usuario o contraseña incorrecta");
        }

        if (authentication.isAuthenticated()) {
            UsuarioPrincipalDTO usuario = (UsuarioPrincipalDTO) authentication.getPrincipal();
            return new LoginAuthResponseDTO(usuario.getId(), usuario.getNombre(), usuario.getRol(), usuario.getCorreo(), jwtService.generateToken(usuario.getId(), usuario.getCorreo()));
        } else {
            throw new AuthFailedException("Invalid email or password");
        }
    }
}
