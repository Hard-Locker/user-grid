package com.halot.nikitazolin.usergrid.validation;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class AgeValidator implements ConstraintValidator<ValidAge, LocalDate> {

  private static final int MIN_AGE = 18;

  @Override
  public void initialize(ValidAge constraintAnnotation) {
  }

  @Override
  public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
    if (birthDate == null) {
      return false;
    }

    return Period.between(birthDate, LocalDate.now()).getYears() >= MIN_AGE;
  }
}
