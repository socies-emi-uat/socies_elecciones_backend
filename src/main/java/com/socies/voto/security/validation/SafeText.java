package com.socies.voto.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating safe text input that prevents SQL injection and XSS
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SafeTextValidator.class)
public @interface SafeText {
    String message() default "El texto contiene caracteres no permitidos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}