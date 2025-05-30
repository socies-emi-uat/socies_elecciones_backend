package com.socies.voto.controllers.administrador;

import com.socies.voto.dtos.usuario.UsuarioCreateDTO;
import com.socies.voto.dtos.usuario.UsuarioDTO;
import com.socies.voto.dtos.usuario.UsuarioUpdateDTO;
import com.socies.voto.services.UsuarioService;
import com.socies.voto.utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrador/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<UsuarioDTO>>> getAllUsers() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsers();
        ResponseWrapper<List<UsuarioDTO>> response = new ResponseWrapper<>(true, "Usuarios obtenidos correctamente.", usuarios);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Crear un usuario
    @PostMapping
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> createUser(@RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioDTO usuario = usuarioService.createUsuario(usuarioCreateDTO);
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(true, "Usuario creado exitosamente.", usuario);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Obtener un usuario por ID
    @GetMapping("/{id_usuario}")
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> getUserById(@PathVariable Long id_usuario) {
        UsuarioDTO usuario = usuarioService.getUserById(id_usuario);
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(true, "Usuario encontrado.", usuario);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
    @PutMapping("/{id_usuario}")
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> updateUser(@PathVariable Long id_usuario, @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        UsuarioDTO usuario = usuarioService.updateUser(id_usuario, usuarioUpdateDTO);
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(true, "Usuario actualizado exitosamente.", usuario);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    */

    @PatchMapping("/{id_usuario}/estado")
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> disableOrEnableUser(@PathVariable Long id_usuario) {
        String estado_usuario = usuarioService.disableOrEnableUser(id_usuario);
        ResponseWrapper<UsuarioDTO> response = new ResponseWrapper<>(true, "Usuario "+estado_usuario, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
