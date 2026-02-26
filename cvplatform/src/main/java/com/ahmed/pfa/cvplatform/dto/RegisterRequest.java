package com.ahmed.pfa.cvplatform.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO pour la requête d'inscription
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Le nom est requis")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est requis")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;

    @NotBlank(message = "L'email est requis")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 6, max = 100, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Le mot de passe doit contenir au moins une lettre et un chiffre"
    )
    private String motDePasse;

    @NotBlank(message = "Le rôle est requis")
    @Pattern(regexp = "ETUDIANT|ADMIN", message = "Le rôle doit être ETUDIANT ou ADMIN")
    private String role;
}