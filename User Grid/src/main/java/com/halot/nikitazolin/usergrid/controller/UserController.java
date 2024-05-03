package com.halot.nikitazolin.usergrid.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.halot.nikitazolin.usergrid.model.User;
import com.halot.nikitazolin.usergrid.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/hello")
  public ResponseEntity<String> sayHello() {
    String goodbye = "Hello";
    log.debug("Was call @GetMapping with data: " + goodbye);

    return ResponseEntity.ok(goodbye);
  }

  @PostMapping("/add")
  public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
    log.info("Attempting to create a new user with details: {}", user);
    Optional<User> createdUser = userService.addUser(user);

    if (createdUser.isPresent()) {
      log.info("User created successfully: " + createdUser.get());
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser.get());
    } else {
      log.warn("Failed to create user due to conflict: " + user);
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @PutMapping("/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody User user) {
    log.info("Attempting to update user with new details: " + user);
    Optional<User> updatedUser = userService.updateUser(userId, user);

    return updatedUser.map(u -> {
      log.info("User updated successfully: " + u);
      return ResponseEntity.ok(u);
    }).orElseGet(() -> {
      log.warn("Failed to find user with id: {} for update", userId);
      return ResponseEntity.notFound().build();
    });
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<?> patchUser(@PathVariable Long userId, @RequestBody Map<String, Object> updateFields) {
    log.info("Attempting to patch user with id: {} with updates: {}", userId, updateFields);
    Optional<User> patchedUser = userService.updateUserFields(userId, updateFields);

    return patchedUser.map(u -> {
      log.info("User with id: {} patched successfully.", userId);
      return ResponseEntity.ok(u);
    }).orElseGet(() -> {
      log.warn("Failed to find user with id: {} for patching", userId);
      return ResponseEntity.notFound().build();
    });
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    log.info("Attempting to delete user with id: {}", userId);
    boolean isDeleted = userService.deleteUser(userId);

    if (isDeleted) {
      log.info("User with id: {} deleted successfully", userId);
      return ResponseEntity.noContent().build();
    } else {
      log.warn("Failed to delete user with id: {}", userId);
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/find/byDate")
  public ResponseEntity<List<User>> findUsersByBirthDates(@RequestParam LocalDate begin, @RequestParam LocalDate end) {
    log.info("Searching for users birth date between {} and {}", begin, end);
    List<User> users = userService.findUsersByBirthDateRange(begin, end);

    if (users.isEmpty()) {
      log.info("No users found birth date between {} and {}", begin, end);
    } else {
      log.info("Found {} users birth date between {} and {}", users.size(), begin, end);
    }

    return ResponseEntity.ok(users);
  }

  @GetMapping("/find/all")
  public ResponseEntity<List<User>> getAllUsers() {
    log.info("Retrieving all users");
    List<User> users = userService.getAllUsers();

    if (users.isEmpty()) {
      log.info("No users found.");
    } else {
      log.info("Retrieved {} users in total.", users.size());
    }

    return ResponseEntity.ok(users);
  }
}
