package com.halot.nikitazolin.usergrid.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
public @interface ValidAge {

  String message() default "Invalid age, user must be at least 18 years old";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
