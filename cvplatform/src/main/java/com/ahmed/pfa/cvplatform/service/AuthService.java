package com.ahmed.pfa.cvplatform.service;

import com.ahmed.pfa.cvplatform.dto.AuthResponse;
import com.ahmed.pfa.cvplatform.dto.LoginRequest;
import com.ahmed.pfa.cvplatform.dto.RegisterRequest;
import com.ahmed.pfa.cvplatform.exception.EmailAlreadyExistsException;
import com.ahmed.pfa.cvplatform.exception.InvalidCredentialsException;
import com.ahmed.pfa.cvplatform.model.Administrateur;
import com.ahmed.pfa.cvplatform.model.Etudiant;
import com.ahmed.pfa.cvplatform.model.Utilisateur;
import com.ahmed.pfa.cvplatform.repository.AdministrateurRepository;
import com.ahmed.pfa.cvplatform.repository.EtudiantRepository;
import com.ahmed.pfa.cvplatform.repository.UtilisateurRepository;
import com.ahmed.pfa.cvplatform.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Inscription d'un nouvel utilisateur
     * @Transactional: Si erreur après save → rollback
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        // Hacher le mot de passe
        String hashedPassword = passwordEncoder.encode(request.getMotDePasse());

        // Créer selon le rôle
        if ("ETUDIANT".equals(request.getRole())) {
            Etudiant etudiant = new Etudiant();
            etudiant.setNom(request.getNom());
            etudiant.setPrenom(request.getPrenom());
            etudiant.setEmail(request.getEmail());
            etudiant.setMotDePasse(hashedPassword);
            etudiant.setRole("ETUDIANT");

            Etudiant saved = etudiantRepository.save(etudiant);

            return new AuthResponse(
                    "Étudiant créé avec succès",
                    saved.getId(),
                    saved.getEmail()
            );

        } else if ("ADMIN".equals(request.getRole())) {
            Administrateur admin = new Administrateur();
            admin.setNom(request.getNom());
            admin.setPrenom(request.getPrenom());
            admin.setEmail(request.getEmail());
            admin.setMotDePasse(hashedPassword);
            admin.setRole("ADMIN");

            Administrateur saved = administrateurRepository.save(admin);

            return new AuthResponse(
                    "Administrateur créé avec succès",
                    saved.getId(),
                    saved.getEmail()
            );

        } else {
            throw new IllegalArgumentException("Rôle non valide. Utilisez ETUDIANT ou ADMIN");
        }
    }

    /**
     * Connexion - Lecture seule
     * @Transactional(readOnly = true): Optimisation
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Chercher l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail());

        if (utilisateur == null) {
            throw new InvalidCredentialsException();
        }

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new InvalidCredentialsException();
        }

        // Générer token
        String token = jwtUtil.generateToken(
                utilisateur.getEmail(),
                utilisateur.getId()
        );

        return new AuthResponse(
                "Connexion réussie",
                utilisateur.getId(),
                utilisateur.getEmail(),
                token
        );
    }
}