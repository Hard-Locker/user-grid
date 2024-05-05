package com.halot.nikitazolin.usergrid.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
class CustomExceptionHandlerTest {

  @InjectMocks
  private CustomExceptionHandler customExceptionHandler;

  @Test
  void handleValidationExceptions_InvalidData_BadRequest() {
    // Arrange
    WebDataBinder binder = new WebDataBinder(null, "objectName");
    binder.getBindingResult().addError(new FieldError("objectName", "field", "must not be null"));
    MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, binder.getBindingResult());

    // Act
    ResponseEntity<Map<String, String>> response = customExceptionHandler.handleValidationExceptions(ex);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("must not be null", response.getBody().get("field"));
  }

  @Test
  void handleJsonProcessingException_InvalidJson_InternalServerError() {
    // Arrange
    JsonProcessingException exception = new JsonProcessingException("JSON error") {
      private static final long serialVersionUID = 1L;
    };

    // Act
    ResponseEntity<String> response = customExceptionHandler.handleJsonProcessingException(exception);

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Error with processing JSON", response.getBody());
  }
}
