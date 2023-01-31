package com.spring.todo.api.todolistapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import jakarta.validation.constraints.Pattern;

// This class is used to store the user details in the database.
// it implements the Serializable interface which is used to convert the object into a byte stream.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {

  private static final long serialVersionUID = -6986746375915710855L;

  @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must be alphanumeric with no spaces")
  private String username;
  @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Password must be alphanumeric with no spaces")
  private String password;
}
