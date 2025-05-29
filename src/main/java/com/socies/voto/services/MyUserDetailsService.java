package com.socies.voto.services;

import com.socies.voto.dtos.usuario.UsuarioPrincipalDTO;
import com.socies.voto.models.Usuario;
import com.socies.voto.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public UsuarioPrincipalDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.getUsuarioByCorreo(username).orElseThrow(() -> new UsernameNotFoundException("El usuario no existe."));
        if (usuario == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("Usuario no encontrado.");
        }

        return new UsuarioPrincipalDTO(usuario);
    }
}