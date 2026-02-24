package com.ahmed.pfa.cvplatform.service;

import com.ahmed.pfa.cvplatform.dto.UpdateProfileRequest;
import com.ahmed.pfa.cvplatform.dto.UserProfileResponse;
import com.ahmed.pfa.cvplatform.model.Administrateur;
import com.ahmed.pfa.cvplatform.model.Etudiant;
import com.ahmed.pfa.cvplatform.model.Utilisateur;
import com.ahmed.pfa.cvplatform.repository.AdministrateurRepository;
import com.ahmed.pfa.cvplatform.repository.EtudiantRepository;
import com.ahmed.pfa.cvplatform.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    // Récupérer le profil d'un utilisateur par ID
    public UserProfileResponse getUserProfile(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return mapToProfileResponse(user);
    }

    // Récupérer tous les utilisateurs (Admin uniquement)
    public List<UserProfileResponse> getAllUsers() {
        List<Utilisateur> users = utilisateurRepository.findAll();
        return users.stream()
                .map(this::mapToProfileResponse)
                .collect(Collectors.toList());
    }

    // Mettre à jour le profil
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Mise à jour des champs communs
        if (request.getNom() != null) {
            user.setNom(request.getNom());
        }
        if (request.getPrenom() != null) {
            user.setPrenom(request.getPrenom());
        }

        // Mise à jour des champs spécifiques selon le type
        if (user instanceof Etudiant) {
            Etudiant etudiant = (Etudiant) user;
            if (request.getNiveauEtude() != null) {
                etudiant.setNiveauEtude(request.getNiveauEtude());
            }
            if (request.getDomaineEtude() != null) {
                etudiant.setDomaineEtude(request.getDomaineEtude());
            }
            if (request.getUniversite() != null) {
                etudiant.setUniversite(request.getUniversite());
            }
            etudiantRepository.save(etudiant);
        } else if (user instanceof Administrateur) {
            Administrateur admin = (Administrateur) user;
            if (request.getPermissions() != null) {
                admin.setPermissions(request.getPermissions());
            }
            administrateurRepository.save(admin);
        } else {
            utilisateurRepository.save(user);
        }

        return mapToProfileResponse(user);
    }

    // Supprimer un utilisateur (Admin uniquement)
    public void deleteUser(Long userId) {
        if (!utilisateurRepository.existsById(userId)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        utilisateurRepository.deleteById(userId);
    }

    // Mapper Utilisateur vers UserProfileResponse
    private UserProfileResponse mapToProfileResponse(Utilisateur user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        if (user instanceof Etudiant) {
            Etudiant etudiant = (Etudiant) user;
            response.setNiveauEtude(etudiant.getNiveauEtude());
            response.setDomaineEtude(etudiant.getDomaineEtude());
            response.setUniversite(etudiant.getUniversite());
        } else if (user instanceof Administrateur) {
            Administrateur admin = (Administrateur) user;
            response.setPermissions(admin.getPermissions());
        }

        return response;
    }
}