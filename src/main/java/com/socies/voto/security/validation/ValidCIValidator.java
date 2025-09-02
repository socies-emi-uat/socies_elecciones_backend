package com.socies.voto.security.validation;

import com.socies.voto.security.InputSanitizer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for CI (Cedula de Identidad) format
 */
public class ValidCIValidator implements ConstraintValidator<ValidCI, String> {

    private final InputSanitizer inputSanitizer = new InputSanitizer();

    @Override
    public void initialize(ValidCI constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null validation
        }

        try {
            // First sanitize to check for security threats
            inputSanitizer.sanitizeInput(value);
            // Then validate CI format
            return inputSanitizer.isValidCI(value);
        } catch (SecurityException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "La cédula contiene caracteres peligrosos")
                .addConstraintViolation();
            return false;
        }
    }
}