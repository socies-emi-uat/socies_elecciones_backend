package com.socies.voto.dtos.usuario;

import com.socies.voto.models.Usuario;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioPrincipalDTO implements UserDetails {

    private final Usuario usuario;

    public UsuarioPrincipalDTO(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Asegúrate de que coincida con el nombre real del campo en Rol
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getTipo_rol()));
    }

    public Long getId() {
        return usuario.getId();
    }

    public String getCorreo() {
        return usuario.getCorreo();
    }

    public String getNombre() {
        return usuario.getNombre()
                + " "
                + usuario.getApellidoPaterno()
                + " "
                + usuario.getApellidoMaterno();
    }

    public String getRol() {
        return usuario.getRol().getTipo_rol();
    }

    public boolean puedeVotar() {
        return usuario.isEstado(); // es falso, no puede votar - true si.
    }

    @Override
    public String getPassword() {
        return usuario.getContrasenaHash();
    }

    @Override
    public String getUsername() {
        // Puedes decidir si usar cedulaIdentidad o correo como nombre de usuario
        return usuario.getCedulaIdentidad();
    }

    public boolean isAdmin() {
        return "Administrador".equals(usuario.getRol().getTipo_rol());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Podrías adaptarlo a lógica real
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Podrías adaptarlo a lógica real
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Podrías adaptarlo a lógica real
    }

    @Override
    public boolean isEnabled() {
        // Ejemplo usando el campo estado para habilitar la cuenta
        return usuario.is_deleted();
    }
}
