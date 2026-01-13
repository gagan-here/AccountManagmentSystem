package com.ams.auth_service.dto;

import com.ams.auth_service.enums.Roles;
import java.util.Set;
import lombok.Data;

@Data
public class SignupRequestDto {
  private String email;
  private String password;
  private String name;
  private Set<Roles> roles;
}
