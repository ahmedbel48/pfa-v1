package com.ahmed.pfa.cvplatform.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String nom;
    private String prenom;

    // Pour Etudiant
    private String niveauEtude;
    private String domaineEtude;
    private String universite;

    // Pour Administrateur
    private String permissions;
}