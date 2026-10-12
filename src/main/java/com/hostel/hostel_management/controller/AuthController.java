
package com.hostel.hostel_management.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostel.hostel_management.dto.LoginRequest;
import com.hostel.hostel_management.dto.RegisterRequest;
import com.hostel.hostel_management.entity.User;
import com.hostel.hostel_management.repository.UserRepository;
import com.hostel.hostel_management.security.JwtService;
import com.hostel.hostel_management.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // ==========================================
    // REGISTER HOSTELLER
    // ==========================================

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        try {
            String fullName = request.getFullName().trim();
            String email = request.getEmail().trim().toLowerCase();
            String password = request.getPassword();

            com.hostel.hostel_management.entity.Role role = com.hostel.hostel_management.entity.Role.HOSTELER;
            if (request.getRole() != null && !request.getRole().isBlank()) {
                String r = request.getRole().trim().toUpperCase();
                if ("OWNER".equals(r) || "WARDEN".equals(r)) {
                    role = com.hostel.hostel_management.entity.Role.WARDEN;
                } else if ("ADMIN".equals(r)) {
                    role = com.hostel.hostel_management.entity.Role.ADMIN;
                } else {
                    role = com.hostel.hostel_management.entity.Role.HOSTELER;
                }
            }

            User user = userService.registerUser(
                    fullName,
                    email,
                    password,
                    request.getPhoneNumber(),
                    request.getCourse(),
                    role
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message", "Registration successful",
                            "id", user.getId(),
                            "fullName", user.getFullName(),
                            "email", user.getEmail(),
                            "role", user.getRole().name()
                    )
            );

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", exception.getMessage()
                    )
            );
        }
    }

    // ==========================================
    // LOGIN HOSTELLER OR AUTHORIZED OWNER
    // ==========================================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                request.getPassword()
                        )
                );

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Login successful",
                        "token", token,
                        "tokenType", "Bearer",
                        "id", user.getId(),
                        "fullName", user.getFullName(),
                        "email", user.getEmail(),
                        "role", user.getRole().name()
                )
        );
    }
}
