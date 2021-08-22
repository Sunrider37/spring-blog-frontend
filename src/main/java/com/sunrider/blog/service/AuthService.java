package com.sunrider.blog.service;

import com.sunrider.blog.dto.AuthenticationResponse;
import com.sunrider.blog.dto.LoginRequest;
import com.sunrider.blog.dto.RegisterRequest;
import com.sunrider.blog.model.User;
import com.sunrider.blog.repository.UserRepository;
import com.sunrider.blog.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        userRepository.save(user);

    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
      Authentication authenticate =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String authenticationToken = jwtTokenProvider.generateToken(authenticate);
        return new AuthenticationResponse(authenticationToken, loginRequest.getUsername());

    }

    public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
       org.springframework.security.core.userdetails.User principal
               =(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return Optional.of(principal);
    }
}
