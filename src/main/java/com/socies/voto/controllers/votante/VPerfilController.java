package com.socies.voto.controllers.votante;

import com.socies.voto.dtos.usuario.UsuarioDTO;
import com.socies.voto.security.AuditLogService;
import com.socies.voto.security.DataAccessValidator;
import com.socies.voto.services.JWTService;
import com.socies.voto.services.UsuarioService;
import com.socies.voto.utils.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votante/perfil")
public class VPerfilController {

    @Autowired private JWTService jwtService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private DataAccessValidator dataAccessValidator;
    @Autowired private AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<UsuarioDTO>> obtenerPerfil(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            auditLogService.logSecurityViolation("MISSING_TOKEN", "Attempt to access profile without token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>(false, "Token no proporcionado", null));
        }

        String token = authHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        // Validate that user can access their own data
        if (!dataAccessValidator.canAccessUserData(userId)) {
            auditLogService.logSecurityViolation("UNAUTHORIZED_PROFILE_ACCESS", 
                "User attempted to access unauthorized profile data");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseWrapper<>(false, "No tiene permisos para acceder a este perfil", null));
        }

        UsuarioDTO usuarioDTO = usuarioService.getUserById(userId);

        ResponseWrapper<UsuarioDTO> response =
                new ResponseWrapper<>(
                        true, "Perfil del votante obtenido correctamente", usuarioDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
