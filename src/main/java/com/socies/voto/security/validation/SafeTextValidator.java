package com.socies.voto.security.validation;

import com.socies.voto.security.InputSanitizer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for Safe Text input that prevents SQL injection and XSS
 */
public class SafeTextValidator implements ConstraintValidator<SafeText, String> {

    private final InputSanitizer inputSanitizer = new InputSanitizer();

    @Override
    public void initialize(SafeText constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null validation
        }

        try {
            // This will throw SecurityException if input contains dangerous patterns
            inputSanitizer.sanitizeInput(value);
            // Check if it's a safe identifier
            return inputSanitizer.isSafeIdentifier(value);
        } catch (SecurityException e) {
            // Custom message for security violations
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "El texto contiene caracteres no permitidos o patrones peligrosos")
                .addConstraintViolation();
            return false;
        }
    }
}