package com.halot.nikitazolin.usergrid.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.halot.nikitazolin.usergrid.validation.ValidAge;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class User {

  private Long userId;

  @NotBlank(message = "First name is required")
  private String firstName;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @Email(message = "Email should be valid")
  @NotBlank(message = "Email is required")
  private String email;

  @Past(message = "Birth date must be in the past")
  @NotNull(message = "Birth date is required")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @ValidAge
  private LocalDate birthDate;

  private String address;

  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
  private String phoneNumber;
}
