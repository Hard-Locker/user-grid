package com.halot.nikitazolin.usergrid.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AgeValidatorTest {

  private AgeValidator validator;

  @BeforeEach
  void setUp() {
    validator = new AgeValidator();
  }

  @Test
  void isValid_NullBirthDate_ReturnsFalse() {
    assertFalse(validator.isValid(null, null));
  }

  @Test
  void isValid_AgeLessThan18_ReturnsFalse() {
    LocalDate birthDate = LocalDate.now().minusYears(17);
    assertFalse(validator.isValid(birthDate, null));
  }

  @Test
  void isValid_AgeExactly18_ReturnsTrue() {
    LocalDate birthDate = LocalDate.now().minusYears(18);
    assertTrue(validator.isValid(birthDate, null));
  }

  @Test
  void isValid_AgeMoreThan18_ReturnsTrue() {
    LocalDate birthDate = LocalDate.now().minusYears(20);
    assertTrue(validator.isValid(birthDate, null));
  }
}
