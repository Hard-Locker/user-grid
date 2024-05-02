package com.halot.nikitazolin.usergrid.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.halot.nikitazolin.usergrid.model.User;

import jakarta.validation.Valid;

@RestController
public class UserController {

  @GetMapping("/hello")
  public String sayHello() {
    return "Hello";
  }
  
  @GetMapping("/goodbye")
  public String sayGoodbye() {
    return "Goodbye";
  }
  
  @PostMapping("/users")
  public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    return ResponseEntity.ok(user);
  }
}
