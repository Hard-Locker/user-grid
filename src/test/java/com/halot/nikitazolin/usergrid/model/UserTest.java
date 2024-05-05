package com.halot.nikitazolin.usergrid.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUserId(1L);
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("john@mail.com");
    user.setBirthDate(LocalDate.of(1990, 1, 1));
    user.setAddress("Street 123");
    user.setPhoneNumber("+1234567890");
  }

  @Test
  void getUserId_InitializedUser_ReturnsCorrectUserId() {
    assertEquals(1L, user.getUserId());
  }

  @Test
  void getFirstName_InitializedUser_ReturnsCorrectFirstName() {
    assertEquals("John", user.getFirstName());
  }

  @Test
  void setFirstName_NewFirstName_SetsFirstNameCorrectly() {
    user.setFirstName("Jane");

    assertEquals("Jane", user.getFirstName());
  }

  @Test
  void getLastName_InitializedUser_ReturnsCorrectLastName() {
    assertEquals("Doe", user.getLastName());
  }

  @Test
  void setLastName_NewLastName_SetsLastNameCorrectly() {
    user.setLastName("Roe");

    assertEquals("Roe", user.getLastName());
  }

  @Test
  void getEmail_InitializedUser_ReturnsCorrectEmail() {
    assertEquals("john@mail.com", user.getEmail());
  }

  @Test
  void setEmail_NewEmail_SetsEmailCorrectly() {
    user.setEmail("jane@example.com");

    assertEquals("jane@example.com", user.getEmail());
  }

  @Test
  void getBirthDate_InitializedUser_ReturnsCorrectBirthDate() {
    assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
  }

  @Test
  void getAddress_InitializedUser_ReturnsCorrectAddress() {
    assertEquals("Street 123", user.getAddress());
  }

  @Test
  void getPhoneNumber_InitializedUser_ReturnsCorrectPhoneNumber() {
    assertEquals("+1234567890", user.getPhoneNumber());
  }

  @Test
  void equals_SameUserObject_ReturnsTrue() {
    boolean result = user.equals(user);

    assertTrue(result);
  }

  @Test
  void equals_NullObject_ReturnsFalse() {
    boolean result = user.equals(null);

    assertFalse(result);
  }

  @Test
  void equals_UserWithSameAttributes_ReturnsTrue() {
    User sameUser = new User();
    sameUser.setUserId(1L);
    sameUser.setFirstName("John");
    sameUser.setLastName("Doe");
    sameUser.setEmail("john@mail.com");
    sameUser.setBirthDate(LocalDate.of(1990, 1, 1));
    sameUser.setAddress("Street 123");
    sameUser.setPhoneNumber("+1234567890");

    boolean result = user.equals(sameUser);

    assertTrue(result);
  }
}
