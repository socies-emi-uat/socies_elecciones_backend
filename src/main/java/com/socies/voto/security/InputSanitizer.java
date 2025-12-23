package com.socies.voto.security;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/**
 * Utility class for input sanitization to prevent SQL injection and other security vulnerabilities
 */
@Component
public class InputSanitizer {

    // Patterns for detecting potential SQL injection attempts
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
            "('|(\\-\\-)|(;)|(\\|)|(\\*)|(%)|(<)|(>)|(\\?)|"
                    + "(UNION)|(SELECT)|(INSERT)|(DELETE)|(UPDATE)|(CREATE)|(DROP)|(ALTER)|(EXEC)|(EXECUTE)|(SCRIPT))",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    // Pattern for XSS prevention
    private static final Pattern XSS_PATTERN = Pattern.compile(
            "(<script[^>]*>.*?</script>)|(<.*?javascript:.*?>)|(<.*?onload.*?>)|(<.*?onerror.*?>)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    // Pattern for valid identifiers (letters including accents, numbers, underscore, hyphen, spaces)
    private static final Pattern SAFE_IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z0-9_\\-\\s\\u00C0-\\u017F]+$");

    // Pattern for valid email (simpler and more reliable)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Pattern for valid CI (Bolivian ID format)
    private static final Pattern CI_PATTERN = Pattern.compile("^[0-9]{7,10}$");

    /**
     * Sanitizes input string by removing potentially dangerous characters
     */
    public String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        // Remove null bytes
        String sanitized = input.replaceAll("\0", "");

        // Trim whitespace
        sanitized = sanitized.trim();

        // Remove potential SQL injection patterns (be conservative)
        if (SQL_INJECTION_PATTERN.matcher(sanitized).find()) {
            throw new SecurityException("Input contains potentially dangerous SQL patterns");
        }

        // Remove potential XSS patterns
        if (XSS_PATTERN.matcher(sanitized).find()) {
            throw new SecurityException("Input contains potentially dangerous script patterns");
        }

        return sanitized;
    }

    /**
     * Validates that an identifier (like names, usernames) contains only safe characters
     */
    public boolean isSafeIdentifier(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return false;
        }
        return SAFE_IDENTIFIER_PATTERN.matcher(identifier.trim()).matches();
    }

    /**
     * Validates email format
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates CI (Cedula de Identidad) format
     */
    public boolean isValidCI(String ci) {
        if (ci == null || ci.trim().isEmpty()) {
            return false;
        }
        return CI_PATTERN.matcher(ci.trim()).matches();
    }

    /**
     * Validates and sanitizes text input
     */
    public String validateAndSanitizeText(String input, String fieldName) {
        if (input == null) {
            return null;
        }

        String sanitized = sanitizeInput(input);

        if (!isSafeIdentifier(sanitized)) {
            throw new IllegalArgumentException(
                    fieldName + " contains invalid characters. Only letters, numbers, spaces, hyphens and underscores are allowed.");
        }

        return sanitized;
    }

    /**
     * Validates and sanitizes email input
     */
    public String validateAndSanitizeEmail(String email, String fieldName) {
        if (email == null) {
            return null;
        }

        String sanitized = sanitizeInput(email);

        if (!isValidEmail(sanitized)) {
            throw new IllegalArgumentException(fieldName + " has invalid email format.");
        }

        return sanitized;
    }

    /**
     * Validates and sanitizes CI input
     */
    public String validateAndSanitizeCI(String ci, String fieldName) {
        if (ci == null) {
            return null;
        }

        String sanitized = sanitizeInput(ci);

        if (!isValidCI(sanitized)) {
            throw new IllegalArgumentException(
                    fieldName + " must contain only 7-10 digits.");
        }

        return sanitized;
    }

    /**
     * Masks sensitive data for logging purposes
     */
    public String maskSensitiveData(String data) {
        if (data == null || data.length() <= 4) {
            return "****";
        }
        return data.substring(0, 2) + "****" + data.substring(data.length() - 2);
    }
}