package com.socies.voto;

import static org.junit.jupiter.api.Assertions.*;

import com.socies.voto.dtos.usuario.UsuarioCreateDTO;
import com.socies.voto.dtos.usuario.UsuarioDTO;
import com.socies.voto.models.Rol;
import com.socies.voto.models.Usuario;
import com.socies.voto.repositories.RolRepository;
import com.socies.voto.repositories.UsuarioRepository;
import com.socies.voto.services.UsuarioService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class) // No hace falta @SpringBootTest aquí
public class UsuarioServiceTests {

  @InjectMocks private UsuarioService usuarioService;

  @Mock private UsuarioRepository usuarioRepository;

  @Mock private RolRepository rolRepository;

  @Mock private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

  @Test
  void testGetListUsuarios() {
    // Crear un rol de prueba
    Rol rol = new Rol();
    rol.setTipo_rol("ADMIN");

    // Crear UsuarioCreateDTO para inicializar usuarios
    UsuarioCreateDTO dto1 =
        new UsuarioCreateDTO(
            "John",
            "Doe",
            "Smith",
            "password",
            "123456",
            LocalDateTime.of(1990, 1, 1, 0, 0),
            "john.doe@example.com",
            "789456",
            "123456789",
            1L);

    UsuarioCreateDTO dto2 =
        new UsuarioCreateDTO(
            "Jane",
            "Loe",
            "Smith",
            "password",
            "654321",
            LocalDateTime.of(1991, 2, 2, 0, 0),
            "jane.doe@example2.com",
            "789123",
            "987654321",
            1L);

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

  @Test
  void testCreateUsuarioSuccess() {
    // DTO de prueba
    UsuarioCreateDTO createDTO =
        new UsuarioCreateDTO(
            "John",
            "Doe",
            "Smith",
            "password123",
            "123456",
            LocalDateTime.of(1990, 1, 1, 0, 0),
            "john.doe@example.com",
            "789456",
            "123456789",
            1L);

    Rol rol = new Rol();
    rol.setId(1L);
    rol.setTipo_rol("USER");

    // Mock de que no existe el correo
    Mockito.when(usuarioRepository.existsByCorreo(createDTO.getCorreo())).thenReturn(false);
    // Mock de que el rol existe
    Mockito.when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));
    // Mock de codificación de contraseña
    Mockito.when(encoder.encode(createDTO.getPassword())).thenReturn("encodedPassword");
    // Mock de guardado del usuario
    Usuario usuario = new Usuario(createDTO, "encodedPassword", rol);
    Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

    // Ejecutar el servicio
    UsuarioDTO result = usuarioService.createUsuario(createDTO);

    // Verificar resultados
    assertNotNull(result);
    assertEquals("John", result.getNombre());
    assertEquals("john.doe@example.com", result.getCorreo());

    // Verificar llamadas
    Mockito.verify(usuarioRepository).existsByCorreo(createDTO.getCorreo());
    Mockito.verify(rolRepository).findById(1L);
    Mockito.verify(encoder).encode("password123");
    Mockito.verify(usuarioRepository).save(Mockito.any(Usuario.class));
  }
}
