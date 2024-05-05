package com.halot.nikitazolin.usergrid.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

  @RequestMapping("/error")
  public ResponseEntity<String> handleError(HttpServletRequest request) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null) {
      Integer statusCode = Integer.valueOf(status.toString());

      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This page does not exist yet");
      } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Our Engineers are on it");
      }
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
  }
}
