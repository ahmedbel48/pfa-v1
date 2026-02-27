package com.ahmed.pfa.cvplatform.controller;

import com.ahmed.pfa.cvplatform.dto.UpdateProfileRequest;
import com.ahmed.pfa.cvplatform.dto.UserProfileResponse;
import com.ahmed.pfa.cvplatform.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // ✅ Import requis
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // =========================================================================
    // ✅ CONSULTATION ET MISE À JOUR
    // =========================================================================

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        logger.info("User {} récupère profil {}", getAuthenticatedUserEmail(), id);
        UserProfileResponse profile = userService.getUserProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse updated = userService.updateProfile(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // =========================================================================
    // ✅ NOUVEAU ENDPOINT AVEC PAGINATION
    // =========================================================================

    /**
     * Récupérer tous les utilisateurs avec pagination
     */
    @GetMapping("/page")
    public ResponseEntity<Page<UserProfileResponse>> getAllUsersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<UserProfileResponse> users = userService.getAllUsersPage(page, size);
        return ResponseEntity.ok(users);
    }

    // =========================================================================
    // ✅ ENDPOINTS CLASSIQUES (SANS PAGINATION)
    // =========================================================================

    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        List<UserProfileResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/test")
    public String test() {
        return "User Controller fonctionne!";
    }
}