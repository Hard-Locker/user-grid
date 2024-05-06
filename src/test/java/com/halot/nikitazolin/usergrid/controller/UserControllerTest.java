package com.halot.nikitazolin.usergrid.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.halot.nikitazolin.usergrid.model.User;
import com.halot.nikitazolin.usergrid.service.UserService;

@SpringBootTest
class UserControllerTest {

  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  void setUp() {
    mockMvc = standaloneSetup(userController).build();
  }

  @Test
  void createUser_ValidUser_Success() throws Exception {
    // Arrange
    User newUser = new User();
    newUser.setFirstName("John");
    newUser.setLastName("Doe");
    newUser.setEmail("john.doe@example.com");
    newUser.setBirthDate(LocalDate.of(2000, 1, 1));
    when(userService.addUser(any(User.class))).thenReturn(Optional.of(newUser));

    // Act & Assert
    mockMvc.perform(post("/users/add").contentType(MediaType.APPLICATION_JSON).content(
        "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"birthDate\": \"2000-01-01\" }"))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"));

    verify(userService).addUser(any(User.class));
  }

  @Test
  void createUser_ValidUser_Conflict() throws Exception {
    // Arrange
    User newUser = new User();
    newUser.setFirstName("John");
    newUser.setLastName("Doe");
    newUser.setEmail("john.doe@example.com");
    newUser.setBirthDate(LocalDate.of(2000, 1, 1));
    when(userService.addUser(any(User.class))).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc.perform(post("/users/add").contentType(MediaType.APPLICATION_JSON).content(
        "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"birthDate\": \"2000-01-01\" }"))
        .andExpect(status().isConflict());

    verify(userService).addUser(any(User.class));
  }

  @Test
  void createUser_InvalidEmail_BadRequest() throws Exception {
    // Arrange
    User newUser = new User();
    newUser.setFirstName("John");
    newUser.setLastName("Doe");
    newUser.setEmail("not-an-email");
    newUser.setBirthDate(LocalDate.of(2000, 1, 1));

    // Act & Assert
    mockMvc.perform(post("/users/add").contentType(MediaType.APPLICATION_JSON).content(
        "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"not-an-email\", \"birthDate\": \"2000-01-01\" }"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createUser_InvalidAge_BadRequest() throws Exception {
    // Arrange
    User newUser = new User();
    newUser.setFirstName("John");
    newUser.setLastName("Doe");
    newUser.setEmail("john.doe@example.com");
    newUser.setBirthDate(LocalDate.now());

    // Act & Assert
    mockMvc.perform(post("/users/add").contentType(MediaType.APPLICATION_JSON).content(
        "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"birthDate\": \""
            + LocalDate.now() + "\" }"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateUser_UserExists_Updated() throws Exception {
    // Arrange
    Long userId = 1L;
    User existingUser = new User();
    existingUser.setUserId(userId);
    existingUser.setFirstName("John");
    existingUser.setLastName("Doe");
    existingUser.setEmail("john.doe@example.com");
    existingUser.setBirthDate(LocalDate.of(1990, 1, 1));

    when(userService.updateUser(eq(userId), any(User.class))).thenReturn(Optional.of(existingUser));

    // Act & Assert
    mockMvc.perform(put("/users/{userId}", userId).contentType(MediaType.APPLICATION_JSON).content(
        "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"birthDate\": \"1990-01-01\" }"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"));

    verify(userService).updateUser(eq(userId), any(User.class));
  }

  @Test
  void updateUser_UserNotExists_NotFound() throws Exception {
    // Arrange
    Long userId = 1L;
    when(userService.updateUser(eq(userId), any(User.class))).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc.perform(put("/users/{userId}", userId).contentType(MediaType.APPLICATION_JSON).content(
        "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"birthDate\": \"1990-01-01\" }"))
        .andExpect(status().isNotFound());

    verify(userService).updateUser(eq(userId), any(User.class));
  }

  @Test
  void patchUser_UserExists_Updated() throws Exception {
    // Arrange
    Long userId = 1L;
    String updateJson = "{\"email\": \"new.email@example.com\", \"firstName\": \"NewFirstName\"}";

    User patchedUser = new User();
    patchedUser.setUserId(userId);
    patchedUser.setEmail("new.email@example.com");
    patchedUser.setFirstName("NewFirstName");

    when(userService.updateUserFields(userId, Map.of("email", "new.email@example.com", "firstName", "NewFirstName")))
        .thenReturn(Optional.of(patchedUser));

    // Act & Assert
    mockMvc.perform(patch("/users/{userId}", userId).contentType(MediaType.APPLICATION_JSON).content(updateJson))
        .andExpect(status().isOk()).andExpect(jsonPath("$.email").value("new.email@example.com"))
        .andExpect(jsonPath("$.firstName").value("NewFirstName"));

    verify(userService).updateUserFields(userId, Map.of("email", "new.email@example.com", "firstName", "NewFirstName"));
  }

  @Test
  void patchUser_UserNotExists_NotFound() throws Exception {
    // Arrange
    Long userId = 1L;
    String updateJson = "{\"email\": \"new.email@example.com\"}";

    when(userService.updateUserFields(userId, Map.of("email", "new.email@example.com"))).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc.perform(patch("/users/{userId}", userId).contentType(MediaType.APPLICATION_JSON).content(updateJson))
        .andExpect(status().isNotFound());

    verify(userService).updateUserFields(userId, Map.of("email", "new.email@example.com"));
  }

  @Test
  void deleteUser_UserExists_NoContent() throws Exception {
    // Arrange
    Long userId = 1L;
    when(userService.deleteUser(userId)).thenReturn(true);

    // Act & Assert
    mockMvc.perform(delete("/users/{userId}", userId)).andExpect(status().isNoContent());

    verify(userService).deleteUser(userId);
  }

  @Test
  void deleteUser_UserNotExists_NotFound() throws Exception {
    // Arrange
    Long userId = 1L;
    when(userService.deleteUser(userId)).thenReturn(false);

    // Act & Assert
    mockMvc.perform(delete("/users/{userId}", userId)).andExpect(status().isNotFound());

    verify(userService).deleteUser(userId);
  }

  @Test
  void findUserById_UserExists_Ok() throws Exception {
    // Arrange
    Long userId = 1L;
    User foundUser = new User();
    foundUser.setUserId(userId);
    when(userService.findUserById(userId)).thenReturn(Optional.of(foundUser));

    // Act & Assert
    mockMvc.perform(get("/users/find/byId").param("userId", String.valueOf(userId))).andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(userId));

    verify(userService).findUserById(userId);
  }

  @Test
  void findUserById_UserNotFound_NotFound() throws Exception {
    // Arrange
    Long userId = 1L;
    when(userService.findUserById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc.perform(get("/users/find/byId").param("userId", String.valueOf(userId))).andExpect(status().isNotFound());

    verify(userService).findUserById(userId);
  }

  @Test
  void findUsersByBirthDates_UsersExists_Ok() throws Exception {
    // Arrange
    LocalDate beginDate = LocalDate.of(1990, 1, 1);
    LocalDate endDate = LocalDate.of(2000, 12, 31);
    User user1 = new User();
    User user2 = new User();
    List<User> users = Arrays.asList(user1, user2);
    when(userService.findUsersByBirthDateRange(beginDate, endDate)).thenReturn(users);

    // Act & Assert
    mockMvc.perform(get("/users/find/byDate").param("begin", beginDate.toString()).param("end", endDate.toString()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2));

    verify(userService).findUsersByBirthDateRange(beginDate, endDate);
  }

  @Test
  void findUsersByBirthDates_UsersNotExists_Ok() throws Exception {
    // Arrange
    LocalDate beginDate = LocalDate.of(1990, 1, 1);
    LocalDate endDate = LocalDate.of(2000, 12, 31);
    when(userService.findUsersByBirthDateRange(beginDate, endDate)).thenReturn(Collections.emptyList());

    // Act & Assert
    mockMvc.perform(get("/users/find/byDate").param("begin", beginDate.toString()).param("end", endDate.toString()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(0));

    verify(userService).findUsersByBirthDateRange(beginDate, endDate);
  }

  @Test
  void getAllUsers_UserExists_Ok() throws Exception {
    // Arrange
    User user1 = new User();
    User user2 = new User();
    List<User> users = Arrays.asList(user1, user2);
    when(userService.getAllUsers()).thenReturn(users);

    // Act & Assert
    mockMvc.perform(get("/users/find/all")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2));

    verify(userService).getAllUsers();
  }

  @Test
  void getAllUsers_UserNotExists_Ok() throws Exception {
    // Arrange
    when(userService.getAllUsers()).thenReturn(Collections.emptyList());

    // Act & Assert
    mockMvc.perform(get("/users/find/all")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(0));

    verify(userService).getAllUsers();
  }
}
