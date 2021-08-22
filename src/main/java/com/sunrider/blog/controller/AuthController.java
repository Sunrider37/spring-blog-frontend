package com.sunrider.blog.controller;

import com.sunrider.blog.dto.AuthenticationResponse;
import com.sunrider.blog.dto.LoginRequest;
import com.sunrider.blog.dto.RegisterRequest;
import com.sunrider.blog.model.User;
import com.sunrider.blog.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return ResponseEntity.ok("User has been created");
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
       return authService.login(loginRequest);
    }
}
