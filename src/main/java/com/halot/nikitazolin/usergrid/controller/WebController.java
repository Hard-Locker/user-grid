package com.halot.nikitazolin.usergrid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

  @GetMapping("/menu")
  public String menuPage(Model model) {
    return "menu";
  }

  @GetMapping("/about")
  public String about() {
    return "about";
  }

  @GetMapping("/form/add")
  public String addUserForm() {
    return "add-user";
  }

  @GetMapping("/form/update")
  public String updateUserForm() {
    return "update-user";
  }

  @GetMapping("/form/delete")
  public String deleteUser() {
    return "delete-user";
  }

  @GetMapping("/find/find-all-users")
  public String findAllUsers() {
    return "find-all-users";
  }

  @GetMapping("/find/find-by-birth-date-range")
  public String findUsersByBirthDateRange() {
    return "find-by-birth-date-range";
  }
}
