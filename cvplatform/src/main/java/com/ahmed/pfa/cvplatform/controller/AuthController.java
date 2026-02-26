package com.ahmed.pfa.cvplatform.controller;

import com.ahmed.pfa.cvplatform.dto.AuthResponse;
import com.ahmed.pfa.cvplatform.dto.LoginRequest;
import com.ahmed.pfa.cvplatform.dto.RegisterRequest;
import com.ahmed.pfa.cvplatform.service.AuthService;
import jakarta.validation.Valid; // <--- TRÈS IMPORTANT : L'import pour @Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint pour l'inscription avec @Valid
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint pour la connexion avec @Valid
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de test
     */
    @GetMapping("/test")
    public String test() {
        return "Auth Controller fonctionne!";
    }
}