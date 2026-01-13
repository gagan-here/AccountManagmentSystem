package com.ams.auth_service.controller;

import com.ams.auth_service.dto.LoginRequestDto;
import com.ams.auth_service.dto.SignupRequestDto;
import com.ams.auth_service.dto.UserDto;
import com.ams.auth_service.service.AuthService;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<UserDto> signUp(@RequestBody SignupRequestDto signupRequestDto) {
    UserDto userDto = authService.signUp(signupRequestDto);
    return new ResponseEntity<>(userDto, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto loginRequestDto) {
    String token = authService.login(loginRequestDto);
    Map<String, String> body = Collections.singletonMap("token", token);
    return ResponseEntity.ok(body);
  }
}
