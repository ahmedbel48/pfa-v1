package com.ahmed.pfa.cvplatform.service;

import com.ahmed.pfa.cvplatform.dto.AuthResponse;
import com.ahmed.pfa.cvplatform.dto.LoginRequest;
import com.ahmed.pfa.cvplatform.dto.RegisterRequest;
import com.ahmed.pfa.cvplatform.model.Utilisateur;
import com.ahmed.pfa.cvplatform.repository.UtilisateurRepository;
import com.ahmed.pfa.cvplatform.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Inscription d'un nouvel utilisateur
    public AuthResponse register(RegisterRequest request) {
        // 1. Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // 2. Créer un nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(request.getMotDePasse()); // À crypter plus tard
        utilisateur.setRole(request.getRole());

        // 3. Sauvegarder dans la base de données
        Utilisateur saved = utilisateurRepository.save(utilisateur);

        // 4. Retourner la réponse
        return new AuthResponse(
                "Utilisateur créé avec succès",
                saved.getId(),
                saved.getEmail()
        );
    }

    // Connexion d'un utilisateur
    public AuthResponse login(LoginRequest request) {
        // 1. Chercher l'utilisateur par email
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail());

        // 2. Vérifier si l'utilisateur existe
        if (utilisateur == null) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        // 3. Vérifier le mot de passe
        // (Pour l'instant sans cryptage, on le cryptera plus tard)
        if (!utilisateur.getMotDePasse().equals(request.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        // 4. Générer le token JWT
        String token = jwtUtil.generateToken(
                utilisateur.getEmail(),
                utilisateur.getId()
        );

        // 5. Retourner la réponse avec le token
        return new AuthResponse(
                "Connexion réussie",
                utilisateur.getId(),
                utilisateur.getEmail(),
                token
        );
    }
}