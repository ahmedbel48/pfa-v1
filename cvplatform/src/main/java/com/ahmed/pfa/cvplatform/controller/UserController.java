package com.ahmed.pfa.cvplatform.controller;

import com.ahmed.pfa.cvplatform.dto.UpdateProfileRequest;
import com.ahmed.pfa.cvplatform.dto.UserProfileResponse;
import com.ahmed.pfa.cvplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Récupérer le profil d'un utilisateur
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        UserProfileResponse profile = userService.getUserProfile(id);
        return ResponseEntity.ok(profile);
    }

    /**
     * Récupérer tous les utilisateurs
     */
    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        List<UserProfileResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Mettre à jour le profil
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateProfileRequest request) {
        UserProfileResponse updated = userService.updateProfile(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprimer un utilisateur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint de test
     */
    @GetMapping("/test")
    public String test() {
        return "User Controller fonctionne!";
    }
}