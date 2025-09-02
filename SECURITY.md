# Security and Audit Implementation Guide

This document describes the security and audit features implemented in the elections backend system.

## Security Components Overview

### 1. Input Sanitization (`InputSanitizer`)

Located in `src/main/java/com/socies/voto/security/InputSanitizer.java`

**Features:**
- SQL injection prevention through pattern detection
- XSS attack prevention 
- Input validation for CI (Cédula de Identidad), emails, and text
- Support for Spanish characters (á, é, í, ó, ú, ñ)
- Sensitive data masking for logs

**Usage Examples:**
```java
@Autowired
private InputSanitizer inputSanitizer;

// Sanitize and validate text input
String safeName = inputSanitizer.validateAndSanitizeText(userInput, "Nombre");

// Validate CI format
String safeCI = inputSanitizer.validateAndSanitizeCI(ciInput, "Cédula");

// Validate email
String safeEmail = inputSanitizer.validateAndSanitizeEmail(emailInput, "Email");

// Mask sensitive data for logging
String maskedData = inputSanitizer.maskSensitiveData(sensitiveInfo);
```

### 2. Data Access Validation (`DataAccessValidator`)

Located in `src/main/java/com/socies/voto/security/DataAccessValidator.java`

**Features:**
- Role-based access control validation
- User data ownership validation
- Voting permission validation
- Electoral process access validation

**Usage Examples:**
```java
@Autowired
private DataAccessValidator dataAccessValidator;

// Check if user can access specific user data
if (!dataAccessValidator.canAccessUserData(userId)) {
    throw new SecurityException("No tiene permisos");
}

// Check admin permissions
if (!dataAccessValidator.canPerformAdminAction("CREATE_CANDIDATE", "Candidato")) {
    throw new SecurityException("No tiene permisos de administrador");
}

// Validate voting permissions
if (!dataAccessValidator.canVote(userId, procesoElectoralId)) {
    throw new SecurityException("No puede votar");
}
```

### 3. Audit Logging (`AuditLogService`)

Located in `src/main/java/com/socies/voto/security/AuditLogService.java`

**Features:**
- Comprehensive audit trail for all operations
- IP address tracking
- User action logging
- Security violation logging
- Sensitive operation monitoring

**Usage Examples:**
```java
@Autowired
private AuditLogService auditLogService;

// Log data access
auditLogService.logDataAccess("GET_CANDIDATE", "Candidato", candidateId, "Details");

// Log admin action
auditLogService.logAdminAction("CREATE_USER", "Usuario", "User created successfully");

// Log security violation
auditLogService.logSecurityViolation("UNAUTHORIZED_ACCESS", "Details of violation");

// Log sensitive operation
auditLogService.logSensitiveOperation("VOTE_CAST", "Vote cast in process " + processId);

// Log authentication event
auditLogService.logAuthenticationEvent(username, "LOGIN", true, "Successful login");
```

### 4. Custom Validation Annotations

Located in `src/main/java/com/socies/voto/security/validation/`

**Available Annotations:**
- `@SafeText` - Validates text input against SQL injection and XSS
- `@ValidCI` - Validates CI (Cédula de Identidad) format

**Usage in DTOs:**
```java
public class CandidatoCreateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @SafeText(message = "El nombre contiene caracteres no permitidos")
    private String nombre;
    
    @NotBlank(message = "La cédula es obligatoria")
    @ValidCI(message = "Formato de cédula inválido")
    private String ciCandidato;
    
    @Email(message = "Formato de email inválido")
    private String email;
}
```

## Security Configuration

### Enhanced Spring Security (`SecurityJWTConfig`)

**New Security Headers:**
- HSTS (HTTP Strict Transport Security)
- X-Frame-Options: DENY
- X-Content-Type-Options: nosniff

**CORS Configuration:**
- Secure CORS setup with controlled origins
- Credentials disabled for security

## Implementation Best Practices

### 1. Input Validation Chain
1. **DTO Level**: Use validation annotations (`@SafeText`, `@ValidCI`, `@Email`)
2. **Service Level**: Use `InputSanitizer` for additional validation
3. **Repository Level**: Use parameterized queries (already implemented)

### 2. Access Control Pattern
```java
public SomeDTO someOperation(Long resourceId) {
    // 1. Validate permissions first
    if (!dataAccessValidator.canAccessResource(resourceId)) {
        auditLogService.logSecurityViolation("UNAUTHORIZED_ACCESS", 
            "Attempt to access resource: " + resourceId);
        throw new SecurityException("No tiene permisos");
    }
    
    // 2. Log the access
    auditLogService.logDataAccess("ACCESS_RESOURCE", "Resource", 
        resourceId.toString(), "Authorized access");
    
    // 3. Perform operation
    return performOperation(resourceId);
}
```

### 3. Audit Logging Pattern
```java
public void sensitiveOperation(String data) {
    try {
        // Log the operation attempt
        auditLogService.logSensitiveOperation("OPERATION_START", "Operation details");
        
        // Perform operation
        performSensitiveOperation(data);
        
        // Log success
        auditLogService.logSensitiveOperation("OPERATION_SUCCESS", "Operation completed");
        
    } catch (Exception e) {
        // Log failure
        auditLogService.logSecurityViolation("OPERATION_FAILED", 
            "Operation failed: " + e.getMessage());
        throw e;
    }
}
```

## Testing

### Security Tests (`InputSanitizerTest`)

Located in `src/test/java/com/socies/voto/security/InputSanitizerTest.java`

**Test Coverage:**
- SQL injection prevention (7 test cases)
- XSS prevention (5 test cases)
- Input validation (CI, email, text)
- Data sanitization and masking

**Running Tests:**
```bash
./gradlew test --tests "InputSanitizerTest"
```

## Security Checklist for New Features

When implementing new features, ensure:

- [ ] All user inputs are validated using `@SafeText`, `@ValidCI`, or `@Email` annotations
- [ ] Service methods use `InputSanitizer` for additional validation
- [ ] Access control is validated using `DataAccessValidator`
- [ ] All operations are logged using `AuditLogService`
- [ ] Sensitive data is masked in logs using `inputSanitizer.maskSensitiveData()`
- [ ] Database queries use parameterized statements
- [ ] Error messages don't reveal sensitive information
- [ ] Security tests are written and passing

## Monitoring and Alerting

### Audit Log Format
All audit logs follow this format:
```
AUDIT | User: us****er | IP: 192.168.1.1 | Action: DATA_ACCESS | Details: ... | Timestamp: 2024-...
```

### Log Categories
- `DATA_ACCESS` - Normal data access operations
- `AUTH_EVENT` - Authentication and authorization events
- `ADMIN_ACTION` - Administrative operations
- `SENSITIVE_OP` - Sensitive operations like voting
- `SECURITY_VIOLATION` - Security violations and unauthorized access attempts

### Recommended Monitoring
Monitor these log patterns for security incidents:
- `SECURITY_VIOLATION` entries
- Failed authentication attempts
- Unusual data access patterns
- SQL injection attempt patterns
- XSS attempt patterns

## Future Enhancements

Potential security improvements to consider:

1. **Rate Limiting**: Implement rate limiting for API endpoints
2. **Session Management**: Enhanced session security
3. **Encryption**: Encrypt sensitive data at rest
4. **Two-Factor Authentication**: Add 2FA for admin users
5. **API Key Management**: Implement API key rotation
6. **Security Headers**: Add CSP (Content Security Policy) headers
7. **Vulnerability Scanning**: Regular dependency vulnerability scans