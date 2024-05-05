package com.halot.nikitazolin.usergrid.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.halot.nikitazolin.usergrid.model.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Scope("singleton")
@Getter
@Slf4j
@RequiredArgsConstructor
public class UserService {

  // Fictional analog of database
  private Map<Long, User> users = new ConcurrentHashMap<>();
  private AtomicLong userIdCounter = new AtomicLong(0);

  // We fake behavior of database when adding rows
  public Optional<User> addUser(User user) {
    if (user.getUserId() == null) {
      Long userId = userIdCounter.incrementAndGet();
      user.setUserId(userId);
      users.put(userId, user);
      log.info("Added new user: " + user);

      return Optional.of(user);
    } else if (!users.containsKey(user.getUserId())) {
      users.put(user.getUserId(), user);
      log.info("Added new user: " + user);

      return Optional.of(user);
    } else {
      log.info("Cannot add user, user ID already exists: {}", user);

      return Optional.empty();
    }
  }

  public Optional<User> updateUser(Long userId, User newUser) {
    User existingUser = users.get(userId);

    if (existingUser != null) {
      newUser.setUserId(userId);
      users.put(userId, newUser);
      log.info("User updated: {}", newUser);

      return Optional.of(newUser);
    } else {
      log.info("User with ID {} not found, cannot update", userId);

      return Optional.empty();
    }
  }

  public Optional<User> updateUserFields(Long userId, Map<String, Object> userFields) {
    User existingUser = users.get(userId);

    if (existingUser == null) {
      log.info("User with ID {} not found, cannot patch", userId);
      return Optional.empty();
    }

    userFields.forEach((field, value) -> {
      switch (field) {
      case "firstName":
        if (value instanceof String) {
          existingUser.setFirstName((String) value);
        }
        break;

      case "lastName":
        if (value instanceof String) {
          existingUser.setLastName((String) value);
        }
        break;

      case "email":
        if (value instanceof String) {
          existingUser.setEmail((String) value);
        }
        break;

      case "birthDate":
        if (value instanceof LocalDate) {
          existingUser.setBirthDate((LocalDate) value);
        }
        break;

      case "address":
        if (value instanceof String) {
          existingUser.setAddress((String) value);
        }
        break;

      case "phoneNumber":
        if (value instanceof String) {
          existingUser.setPhoneNumber((String) value);
        }
        break;

      default:
        log.warn("Attempted to update unknown field: {}", field);
      }
    });

    users.put(userId, existingUser);
    log.info("User partially updated: {}", existingUser);

    return Optional.of(existingUser);
  }

  public boolean deleteUser(Long userId) {
    if (users.containsKey(userId)) {
      log.info("Remove user: " + users.get(userId));
      users.remove(userId);

      return true;
    } else {
      log.info("Not find user: " + users.get(userId));

      return false;
    }
  }

  public Optional<User> findUserById(Long userId) {
    User existingUser = users.get(userId);

    if (existingUser != null) {
      log.info("Get user: {}", existingUser);

      return Optional.of(existingUser);
    } else {
      log.info("User with ID {} not found", userId);

      return Optional.empty();
    }
  }

  public List<User> findUsersByBirthDateRange(LocalDate begin, LocalDate end) {
    List<User> findedUsers = users.values().stream().filter(user -> user.getBirthDate() != null)
        .filter(user -> (user.getBirthDate().isEqual(begin) || user.getBirthDate().isAfter(begin))
            && (user.getBirthDate().isEqual(end) || user.getBirthDate().isBefore(end)))
        .toList();

    log.info("Get users quantity: " + findedUsers.size());

    return findedUsers;
  }

  public List<User> getAllUsers() {
    log.info("Get users quantity: " + users.size());

    return new ArrayList<>(users.values());
  }
}
