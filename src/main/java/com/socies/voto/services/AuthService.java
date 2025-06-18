package com.socies.voto.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.socies.voto.dtos.AuthDTO.LoginAuthDTO;
import com.socies.voto.dtos.AuthDTO.LoginAuthResponseDTO;
import com.socies.voto.dtos.usuario.UsuarioPrincipalDTO;
import com.socies.voto.exceptions.Auth.AuthFailedException;
import com.socies.voto.models.Usuario;
import com.socies.voto.repositories.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public LoginAuthResponseDTO verify(LoginAuthDTO loginAuthDTO) {
        String login = loginAuthDTO.getLogin();
        String password = loginAuthDTO.getPassword();

        String correoAutenticacion;

        if (esCorreoValido(login)) {
            correoAutenticacion = login;
        } else {
            Optional<Usuario> optionalUsuario =
                    usuarioRepository.getUsuarioByCedulaIdentidad(login);
            if (optionalUsuario.isEmpty()) {
                throw new AuthFailedException("Usuario o contraseña incorrecta");
            }
            correoAutenticacion = optionalUsuario.get().getCorreo();
        }

        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(correoAutenticacion, password));
        } catch (BadCredentialsException e) {
            throw new AuthFailedException("Usuario o contraseña incorrecta");
        } catch (DisabledException e) {
            throw new AuthFailedException("Cuenta deshabilitada, contacta con el administrador.");
        }

        if (authentication.isAuthenticated()) {
            UsuarioPrincipalDTO usuario = (UsuarioPrincipalDTO) authentication.getPrincipal();
            return new LoginAuthResponseDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getRol(),
                    usuario.getCorreo(),
                    jwtService.generateToken(usuario.getId(), usuario.getCorreo()));
        } else {
            throw new AuthFailedException("Autenticación fallida");
        }
    }

    private boolean esCorreoValido(String login) {
        return login != null && login.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }
}
