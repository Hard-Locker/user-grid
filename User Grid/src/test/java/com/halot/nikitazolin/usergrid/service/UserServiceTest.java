package com.halot.nikitazolin.usergrid.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.halot.nikitazolin.usergrid.model.User;

@SpringBootTest
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void addUser_NewUser_ShouldAddUserSuccessfully() {
    // Arrange
    User newUser = new User();
    newUser.setFirstName("John");

    // Act
    Optional<User> addedUser = userService.addUser(newUser);

    // Assert
    assertTrue(addedUser.isPresent(), "User should be added");
    assertEquals(newUser.getFirstName(), addedUser.get().getFirstName(), "Names should match");
    assertTrue(addedUser.get().getUserId() > 0, "User ID should be assigned");
  }

  @Test
  void addUser_ExistingUserIdNotInDB_ShouldAddUserSuccessfully() {
    // Arrange
    User newUser = new User();
    newUser.setUserId(100L);
    newUser.setFirstName("Jane");

    // Act
    Optional<User> addedUser = userService.addUser(newUser);

    // Assert
    assertTrue(addedUser.isPresent(), "User should be added");
    assertEquals(newUser.getFirstName(), addedUser.get().getFirstName(), "Names should match");
  }

  @Test
  void addUser_ExistingUserIdInDB_ShouldFailToAddUser() {
    // Arrange
    User existingUser = new User();
    existingUser.setUserId(1L);
    existingUser.setFirstName("Existing User");
    userService.addUser(existingUser);

    User newUser = new User();
    newUser.setUserId(1L);
    newUser.setFirstName("New User");

    // Act
    Optional<User> addedUser = userService.addUser(newUser);

    // Assert
    assertFalse(addedUser.isPresent(), "User should not be added");
  }

  @Test
  void updateUser_ExistingUser_ShouldUpdateUserSuccessfully() {
    // Arrange
    User existingUser = new User();
    existingUser.setUserId(1L);
    existingUser.setFirstName("Existing User");
    userService.addUser(existingUser);

    User updatedUser = new User();
    updatedUser.setFirstName("Updated User");

    // Act
    Optional<User> result = userService.updateUser(1L, updatedUser);

    // Assert
    assertTrue(result.isPresent(), "User should be updated");
    assertEquals(1L, result.get().getUserId(), "User ID should remain the same");
    assertEquals("Updated User", result.get().getFirstName(), "User name should be updated");
  }

  @Test
  void updateUser_NonExistingUser_ShouldReturnEmptyOptional() {
    // Arrange
    User newUser = new User();
    newUser.setFirstName("New User");

    // Act
    Optional<User> result = userService.updateUser(999L, newUser);

    // Assert
    assertFalse(result.isPresent(), "Should not update non-existing user");
  }

  @Test
  void updateUserFields_ExistingUser_ShouldUpdateFieldsSuccessfully() {
    // Arrange
    User existingUser = new User();
    existingUser.setUserId(1L);
    userService.addUser(existingUser);

    Map<String, Object> updates = new HashMap<>();
    updates.put("firstName", "John");
    updates.put("lastName", "Doe");
    updates.put("email", "john@example.com");
    updates.put("birthDate", LocalDate.of(1990, 1, 1));

    // Act
    Optional<User> updatedUser = userService.updateUserFields(1L, updates);

    // Assert
    assertTrue(updatedUser.isPresent(), "User should be found and updated");
    assertEquals("John", updatedUser.get().getFirstName());
    assertEquals("Doe", updatedUser.get().getLastName());
    assertEquals("john@example.com", updatedUser.get().getEmail());
    assertEquals(LocalDate.of(1990, 1, 1), updatedUser.get().getBirthDate());
  }

  @Test
  void updateUserFields_NonExistingUser_ShouldReturnEmptyOptional() {
    // Arrange
    Map<String, Object> updates = new HashMap<>();
    updates.put("firstName", "John");

    // Act
    Optional<User> updatedUser = userService.updateUserFields(999L, updates);

    // Assert
    assertFalse(updatedUser.isPresent(), "Should return empty optional for non-existing user");
  }

  @Test
  void updateUserFields_UnknownField_ShouldIgnoreFieldAndLogWarning() {
    // Arrange
    User existingUser = new User();
    existingUser.setUserId(1L);
    userService.addUser(existingUser);

    Map<String, Object> updates = new HashMap<>();
    updates.put("unknownField", "Some value");

    // Act
    Optional<User> updatedUser = userService.updateUserFields(1L, updates);

    // Assert
    assertTrue(updatedUser.isPresent(), "User should be found even with unknown field");
  }

  @Test
  void deleteUser_ExistingUser_ShouldReturnTrue() {
    // Arrange
    User existingUser = new User();
    existingUser.setUserId(1L);
    userService.addUser(existingUser);

    // Act
    boolean result = userService.deleteUser(1L);

    // Assert
    assertTrue(result, "Should return true when deleting an existing user");
  }

  @Test
  void deleteUser_NonExistingUser_ShouldReturnFalse() {
    // Act
    boolean result = userService.deleteUser(999L);

    // Assert
    assertFalse(result, "Should return false when deleting a non-existing user");
  }

  @Test
  void findUsersByBirthDateRange_ValidRange_ShouldReturnUsers() {
    // Arrange
    LocalDate startDate = LocalDate.of(1990, 1, 1);
    LocalDate endDate = LocalDate.of(1995, 12, 31);

    User user1 = new User();
    user1.setUserId(1L);
    user1.setBirthDate(LocalDate.of(1992, 6, 15));
    userService.addUser(user1);

    User user2 = new User();
    user2.setUserId(2L);
    user2.setBirthDate(LocalDate.of(1991, 8, 20));
    userService.addUser(user2);

    User user3 = new User();
    user3.setUserId(3L);
    user3.setBirthDate(LocalDate.of(1985, 5, 10));
    userService.addUser(user3);

    // Act
    List<User> users = userService.findUsersByBirthDateRange(startDate, endDate);

    // Assert
    assertEquals(2, users.size(), "Should find exactly two users");
  }

  @Test
  void findUsersByBirthDateRange_EmptyDB_ShouldReturnEmptyList() {
    // Arrange
    LocalDate startDate = LocalDate.of(1990, 1, 1);
    LocalDate endDate = LocalDate.of(1995, 12, 31);

    // Act
    List<User> users = userService.findUsersByBirthDateRange(startDate, endDate);

    // Assert
    assertTrue(users.isEmpty(), "Should return an empty list if no users exist");
  }

  @Test
  void findUsersByBirthDateRange_NoUsersInDateRange_ShouldReturnEmptyList() {
    // Arrange
    LocalDate startDate = LocalDate.of(1990, 1, 1);
    LocalDate endDate = LocalDate.of(1995, 12, 31);

    User user = new User();
    user.setUserId(1L);
    user.setBirthDate(LocalDate.of(1985, 5, 10));
    userService.addUser(user);

    // Act
    List<User> users = userService.findUsersByBirthDateRange(startDate, endDate);

    // Assert
    assertTrue(users.isEmpty(), "Should return an empty list if no users are in the date range");
  }

  @Test
  void getAllUsers_WhenUsersExist_ShouldReturnAllUsers() {
    // Arrange
    User user1 = new User();
    user1.setUserId(1L);
    userService.addUser(user1);

    User user2 = new User();
    user2.setUserId(2L);
    userService.addUser(user2);

    // Act
    List<User> users = userService.getAllUsers();

    // Assert
    assertEquals(2, users.size(), "Should return all users in the system");
  }

  @Test
  void getAllUsers_WhenNoUsersExist_ShouldReturnEmptyList() {
    // Act
    List<User> users = userService.getAllUsers();

    // Assert
    assertTrue(users.isEmpty(), "Should return an empty list if no users are in the system");
  }
}
