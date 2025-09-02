package com.socies.voto.security;

import com.socies.voto.dtos.usuario.UsuarioPrincipalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Component for validating data access permissions and ensuring users can only access their own data
 */
@Component
public class DataAccessValidator {

    @Autowired
    private AuditLogService auditLogService;

    /**
     * Validates that the current user can access data for the given user ID
     */
    public boolean canAccessUserData(Long requestedUserId) {
        UsuarioPrincipalDTO currentUser = getCurrentUser();
        
        if (currentUser == null) {
            auditLogService.logSecurityViolation("UNAUTHORIZED_ACCESS", 
                "Attempt to access user data without authentication");
            return false;
        }

        // Admin can access any user data
        if (hasRole("ROLE_Administrador")) {
            auditLogService.logDataAccess("ACCESS_USER_DATA", "Usuario", requestedUserId.toString(),
                "Admin accessing user data");
            return true;
        }

        // Users can only access their own data
        if (currentUser.getId().equals(requestedUserId)) {
            auditLogService.logDataAccess("ACCESS_OWN_DATA", "Usuario", requestedUserId.toString(),
                "User accessing own data");
            return true;
        }

        // Log unauthorized access attempt
        auditLogService.logSecurityViolation("UNAUTHORIZED_USER_ACCESS", 
            String.format("User %s attempted to access data for user %s", 
                currentUser.getId(), requestedUserId));
        
        return false;
    }

    /**
     * Validates that the current user can perform administrative actions
     */
    public boolean canPerformAdminAction(String action, String target) {
        UsuarioPrincipalDTO currentUser = getCurrentUser();
        
        if (currentUser == null || !hasRole("ROLE_Administrador")) {
            auditLogService.logSecurityViolation("UNAUTHORIZED_ADMIN_ACTION", 
                String.format("Non-admin user attempted action: %s on %s", action, target));
            return false;
        }

        auditLogService.logAdminAction(action, target, "Admin action authorized");
        return true;
    }

    /**
     * Validates voting permissions - users can only vote once per electoral process
     */
    public boolean canVote(Long userId, Long procesoElectoralId) {
        UsuarioPrincipalDTO currentUser = getCurrentUser();
        
        if (currentUser == null) {
            auditLogService.logSecurityViolation("UNAUTHORIZED_VOTE", 
                "Attempt to vote without authentication");
            return false;
        }

        // Users can only vote as themselves
        if (!currentUser.getId().equals(userId)) {
            auditLogService.logSecurityViolation("VOTE_IMPERSONATION", 
                String.format("User %s attempted to vote as user %s", 
                    currentUser.getId(), userId));
            return false;
        }

        // Must have Votante role
        if (!hasRole("ROLE_Votante")) {
            auditLogService.logSecurityViolation("INVALID_VOTE_ROLE", 
                String.format("User %s with invalid role attempted to vote", userId));
            return false;
        }

        auditLogService.logSensitiveOperation("VOTE_VALIDATION", 
            String.format("Vote validation passed for user %s in process %s", userId, procesoElectoralId));
        
        return true;
    }

    /**
     * Validates that the current user can access electoral process data
     */
    public boolean canAccessElectoralProcess(Long procesoElectoralId) {
        UsuarioPrincipalDTO currentUser = getCurrentUser();
        
        if (currentUser == null) {
            auditLogService.logSecurityViolation("UNAUTHORIZED_PROCESS_ACCESS", 
                "Attempt to access electoral process without authentication");
            return false;
        }

        // Both roles can access electoral process data (for voting and administration)
        auditLogService.logDataAccess("ACCESS_ELECTORAL_PROCESS", "ProcesoElectoral", 
            procesoElectoralId.toString(), "Authorized access to electoral process");
        
        return true;
    }

    /**
     * Gets the currently authenticated user
     */
    private UsuarioPrincipalDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() 
            && authentication.getPrincipal() instanceof UsuarioPrincipalDTO) {
            return (UsuarioPrincipalDTO) authentication.getPrincipal();
        }
        
        return null;
    }

    /**
     * Checks if the current user has the specified role
     */
    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
        }
        
        return false;
    }

    /**
     * Gets the current user ID or null if not authenticated
     */
    public Long getCurrentUserId() {
        UsuarioPrincipalDTO currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }

    /**
     * Checks if the current user is an administrator
     */
    public boolean isCurrentUserAdmin() {
        return hasRole("ROLE_Administrador");
    }

    /**
     * Checks if the current user is a voter
     */
    public boolean isCurrentUserVoter() {
        return hasRole("ROLE_Votante");
    }
}