package com.socies.voto;

import com.socies.voto.dtos.usuario.UsuarioCreateDTO;
import com.socies.voto.dtos.usuario.UsuarioDTO;
import com.socies.voto.models.Rol;
import com.socies.voto.models.Usuario;
import com.socies.voto.repositories.UsuarioRepository;
import com.socies.voto.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // No hace falta @SpringBootTest aquí
public class UsuarioServiceTests {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    void testGetListUsuarios() {
        // Crear un rol de prueba
        Rol rol = new Rol();
        rol.setTipo_rol("ADMIN");

        // Crear UsuarioCreateDTO para inicializar usuarios
        UsuarioCreateDTO dto1 = new UsuarioCreateDTO(
                "John", "Doe", "Smith",
                "password", "123456", LocalDateTime.of(1990, 1, 1, 0, 0),
                "john.doe@example.com", "789456", "123456789", 1L
        );

        UsuarioCreateDTO dto2 = new UsuarioCreateDTO(
                "Jane", "Loe", "Smith",
                "password", "654321", LocalDateTime.of(1991, 2, 2, 0, 0),
                "jane.doe@example2.com", "789123", "987654321", 1L
        );

        // Crear usuarios reales
        Usuario usuario1 = new Usuario(dto1, dto1.getCorreo(), rol);
        Usuario usuario2 = new Usuario(dto2, dto2.getCorreo(), rol);

        // Mock de la base de datos
        List<Usuario> usuarios = List.of(usuario1, usuario2);
        Mockito.when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Ejecutar método a probar
        List<UsuarioDTO> result = usuarioService.getAllUsers();

        // Verificaciones
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getNombre());
        assertEquals("Jane", result.get(1).getNombre());

        // Verificar que findAll() se llamó una vez
        Mockito.verify(usuarioRepository, Mockito.times(1)).findAll();
    }
}
