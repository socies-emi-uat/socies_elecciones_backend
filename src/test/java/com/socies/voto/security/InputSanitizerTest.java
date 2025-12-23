package com.socies.voto.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Security tests for InputSanitizer to prevent SQL injection and XSS attacks
 */
public class InputSanitizerTest {

    private InputSanitizer inputSanitizer;

    @BeforeEach
    void setUp() {
        inputSanitizer = new InputSanitizer();
    }

    @Test
    void testSqlInjectionPrevention() {
        // Test various SQL injection patterns
        String[] maliciousInputs = {
            "'; DROP TABLE users; --",
            "1' OR '1'='1",
            "admin'--",
            "' UNION SELECT * FROM passwords --",
            "'; INSERT INTO users VALUES ('hacker', 'password'); --",
            "1' OR 1=1#",
            "'; EXEC xp_cmdshell('dir'); --"
        };

        for (String maliciousInput : maliciousInputs) {
            assertThrows(SecurityException.class, () -> {
                inputSanitizer.sanitizeInput(maliciousInput);
            }, "Should detect SQL injection pattern: " + maliciousInput);
        }
    }

    @Test
    void testXssPrevention() {
        // Test various XSS patterns
        String[] xssInputs = {
            "<script>alert('XSS')</script>",
            "<img src='x' onerror='alert(1)'>",
            "<div onload='alert(1)'>",
            "<iframe src='javascript:alert(1)'></iframe>",
            "<body onload='alert(1)'>"
        };

        for (String xssInput : xssInputs) {
            assertThrows(SecurityException.class, () -> {
                inputSanitizer.sanitizeInput(xssInput);
            }, "Should detect XSS pattern: " + xssInput);
        }
    }

    @Test
    void testValidInputPasses() {
        // Test that legitimate inputs pass through
        String[] validInputs = {
            "Juan Pérez",
            "123456789",
            "usuario@ejemplo.com",
            "Propuesta para mejorar la educación",
            "Candidato-2024"
        };

        for (String validInput : validInputs) {
            assertDoesNotThrow(() -> {
                String result = inputSanitizer.sanitizeInput(validInput);
                assertEquals(validInput.trim(), result);
            }, "Valid input should pass: " + validInput);
        }
    }

    @Test
    void testCIValidation() {
        // Valid CI numbers
        assertTrue(inputSanitizer.isValidCI("1234567"));
        assertTrue(inputSanitizer.isValidCI("1234567890"));
        
        // Invalid CI numbers
        assertFalse(inputSanitizer.isValidCI("123"));
        assertFalse(inputSanitizer.isValidCI("12345678901"));
        assertFalse(inputSanitizer.isValidCI("12345A7"));
        assertFalse(inputSanitizer.isValidCI("123-456-789"));
        assertFalse(inputSanitizer.isValidCI(null));
        assertFalse(inputSanitizer.isValidCI(""));
    }

    @Test
    void testEmailValidation() {
        // Valid emails
        assertTrue(inputSanitizer.isValidEmail("usuario@ejemplo.com"));
        assertTrue(inputSanitizer.isValidEmail("test.email+tag@example.org"));
        
        // Invalid emails
        assertFalse(inputSanitizer.isValidEmail("invalid-email"));
        assertFalse(inputSanitizer.isValidEmail("@ejemplo.com"));
        assertFalse(inputSanitizer.isValidEmail("usuario@"));
        assertFalse(inputSanitizer.isValidEmail("usuario@.com"));
        assertFalse(inputSanitizer.isValidEmail(null));
        assertFalse(inputSanitizer.isValidEmail(""));
    }

    @Test
    void testSafeIdentifierValidation() {
        // Valid identifiers
        assertTrue(inputSanitizer.isSafeIdentifier("Juan Pérez"));
        assertTrue(inputSanitizer.isSafeIdentifier("Candidato-2024"));
        assertTrue(inputSanitizer.isSafeIdentifier("Usuario_Test"));
        assertTrue(inputSanitizer.isSafeIdentifier("123ABC"));
        
        // Invalid identifiers
        assertFalse(inputSanitizer.isSafeIdentifier("user@domain.com"));
        assertFalse(inputSanitizer.isSafeIdentifier("test<script>"));
        assertFalse(inputSanitizer.isSafeIdentifier("user'name"));
        assertFalse(inputSanitizer.isSafeIdentifier("user;DROP"));
        assertFalse(inputSanitizer.isSafeIdentifier(null));
        assertFalse(inputSanitizer.isSafeIdentifier(""));
    }

    @Test
    void testSensitiveDataMasking() {
        assertEquals("****", inputSanitizer.maskSensitiveData(null));
        assertEquals("****", inputSanitizer.maskSensitiveData(""));
        assertEquals("****", inputSanitizer.maskSensitiveData("123"));
        assertEquals("12****89", inputSanitizer.maskSensitiveData("12345689"));
        assertEquals("us****om", inputSanitizer.maskSensitiveData("user@email.com"));
    }

    @Test
    void testValidateAndSanitizeText() {
        // Valid text
        String result = inputSanitizer.validateAndSanitizeText("Juan Pérez", "Nombre");
        assertEquals("Juan Pérez", result);
        
        // Invalid text with SQL injection
        assertThrows(SecurityException.class, () -> {
            inputSanitizer.validateAndSanitizeText("'; DROP TABLE users; --", "Nombre");
        });
        
        // Invalid text with email format (not allowed in text fields)
        assertThrows(IllegalArgumentException.class, () -> {
            inputSanitizer.validateAndSanitizeText("user@domain.com", "Nombre");
        });
    }

    @Test
    void testValidateAndSanitizeEmail() {
        // Valid email
        String result = inputSanitizer.validateAndSanitizeEmail("user@example.com", "Email");
        assertEquals("user@example.com", result);
        
        // Invalid email format
        assertThrows(IllegalArgumentException.class, () -> {
            inputSanitizer.validateAndSanitizeEmail("invalid-email", "Email");
        });
        
        // Email with SQL injection attempt
        assertThrows(SecurityException.class, () -> {
            inputSanitizer.validateAndSanitizeEmail("user'; DROP TABLE users; --@example.com", "Email");
        });
    }

    @Test
    void testValidateAndSanitizeCI() {
        // Valid CI
        String result = inputSanitizer.validateAndSanitizeCI("1234567", "CI");
        assertEquals("1234567", result);
        
        // Invalid CI format
        assertThrows(IllegalArgumentException.class, () -> {
            inputSanitizer.validateAndSanitizeCI("123ABC", "CI");
        });
        
        // CI with SQL injection attempt
        assertThrows(SecurityException.class, () -> {
            inputSanitizer.validateAndSanitizeCI("123'; DROP TABLE users; --", "CI");
        });
    }
}