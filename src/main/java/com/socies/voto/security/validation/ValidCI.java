package com.socies.voto.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating CI (Cedula de Identidad) format
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCIValidator.class)
public @interface ValidCI {
    String message() default "Formato de cédula de identidad inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}