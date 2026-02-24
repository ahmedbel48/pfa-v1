package com.ahmed.pfa.cvplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String role;

    // Champs spécifiques pour Etudiant
    private String niveauEtude;
    private String domaineEtude;
    private String universite;

    // Champs spécifiques pour Administrateur
    private String permissions;
}