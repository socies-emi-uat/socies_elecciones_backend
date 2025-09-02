package com.socies.voto.security;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Service for audit logging to track user actions and data access
 */
@Service
public class AuditLogService {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    @Autowired
    private InputSanitizer inputSanitizer;

    /**
     * Logs data access events
     */
    public void logDataAccess(String action, String resourceType, String resourceId, String details) {
        String username = getCurrentUsername();
        String clientIp = getClientIp();
        
        auditLogger.info("DATA_ACCESS | User: {} | IP: {} | Action: {} | Resource: {}:{} | Details: {} | Timestamp: {}",
                inputSanitizer.maskSensitiveData(username),
                clientIp,
                action,
                resourceType,
                resourceId,
                details,
                LocalDateTime.now());
    }

    /**
     * Logs authentication events
     */
    public void logAuthenticationEvent(String username, String event, boolean success, String details) {
        String clientIp = getClientIp();
        
        auditLogger.info("AUTH_EVENT | User: {} | IP: {} | Event: {} | Success: {} | Details: {} | Timestamp: {}",
                inputSanitizer.maskSensitiveData(username),
                clientIp,
                event,
                success,
                details,
                LocalDateTime.now());
    }

    /**
     * Logs security violations
     */
    public void logSecurityViolation(String violationType, String details) {
        String username = getCurrentUsername();
        String clientIp = getClientIp();
        
        auditLogger.warn("SECURITY_VIOLATION | User: {} | IP: {} | Type: {} | Details: {} | Timestamp: {}",
                inputSanitizer.maskSensitiveData(username),
                clientIp,
                violationType,
                details,
                LocalDateTime.now());
    }

    /**
     * Logs sensitive operations like vote casting
     */
    public void logSensitiveOperation(String operation, String details) {
        String username = getCurrentUsername();
        String clientIp = getClientIp();
        
        auditLogger.info("SENSITIVE_OP | User: {} | IP: {} | Operation: {} | Details: {} | Timestamp: {}",
                inputSanitizer.maskSensitiveData(username),
                clientIp,
                operation,
                details,
                LocalDateTime.now());
    }

    /**
     * Logs administrative actions
     */
    public void logAdminAction(String action, String targetResource, String details) {
        String username = getCurrentUsername();
        String clientIp = getClientIp();
        
        auditLogger.info("ADMIN_ACTION | Admin: {} | IP: {} | Action: {} | Target: {} | Details: {} | Timestamp: {}",
                inputSanitizer.maskSensitiveData(username),
                clientIp,
                action,
                targetResource,
                details,
                LocalDateTime.now());
    }

    /**
     * Gets the current authenticated username
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "ANONYMOUS";
    }

    /**
     * Gets the client IP address
     */
    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            
            // Check for X-Forwarded-For header (common in load balancers)
            String xForwardedFor = request.getHeader("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }
            
            // Check for X-Real-IP header
            String xRealIp = request.getHeader("X-Real-IP");
            if (xRealIp != null && !xRealIp.isEmpty()) {
                return xRealIp;
            }
            
            // Fall back to remote address
            return request.getRemoteAddr();
        }
        return "UNKNOWN";
    }
}