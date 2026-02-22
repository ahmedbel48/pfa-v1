package com.ahmed.pfa.cvplatform.controller;

import com.ahmed.pfa.cvplatform.dto.AuthResponse;
import com.ahmed.pfa.cvplatform.dto.LoginRequest;
import com.ahmed.pfa.cvplatform.dto.RegisterRequest;
import com.ahmed.pfa.cvplatform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint pour l'inscription
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(e.getMessage(), null, null));
        }
    }

    // Endpoint pour la connexion
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(e.getMessage(), null, null));
        }
    }

    // Endpoint de test
    @GetMapping("/test")
    public String test() {
        return "Auth Controller fonctionne!";
    }
}
