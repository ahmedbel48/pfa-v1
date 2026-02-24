package com.ahmed.pfa.cvplatform.service;

import com.ahmed.pfa.cvplatform.dto.OffreEmploiRequest;
import com.ahmed.pfa.cvplatform.dto.OffreEmploiResponse;
import com.ahmed.pfa.cvplatform.model.Etudiant;
import com.ahmed.pfa.cvplatform.model.OffreEmploi;
import com.ahmed.pfa.cvplatform.repository.EtudiantRepository;
import com.ahmed.pfa.cvplatform.repository.OffreEmploiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OffreEmploiService {

    @Autowired
    private OffreEmploiRepository offreEmploiRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    // Créer une offre d'emploi
    public OffreEmploiResponse createOffre(OffreEmploiRequest request) {
        OffreEmploi offre = new OffreEmploi();
        offre.setTitre(request.getTitre());
        offre.setEntreprise(request.getEntreprise());
        offre.setDescription(request.getDescription());
        offre.setLocalisation(request.getLocalisation());
        offre.setTypeContrat(request.getTypeContrat());
        offre.setNiveauExperience(request.getNiveauExperience());
        offre.setSalaireMin(request.getSalaireMin());
        offre.setSalaireMax(request.getSalaireMax());
        offre.setCompetences(request.getCompetences());
        offre.setDatePublication(LocalDateTime.now());
        offre.setDateExpiration(request.getDateExpiration());
        offre.setActive(true);

        // Si un étudiant est spécifié
        if (request.getEtudiantId() != null) {
            Etudiant etudiant = etudiantRepository.findById(request.getEtudiantId())
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
            offre.setEtudiant(etudiant);
        }

        OffreEmploi saved = offreEmploiRepository.save(offre);
        return mapToResponse(saved);
    }

    // Récupérer toutes les offres actives
    public List<OffreEmploiResponse> getAllOffresActives() {
        List<OffreEmploi> offres = offreEmploiRepository.findByActiveTrue();
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Récupérer toutes les offres
    public List<OffreEmploiResponse> getAllOffres() {
        List<OffreEmploi> offres = offreEmploiRepository.findAll();
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Récupérer une offre par ID
    public OffreEmploiResponse getOffreById(Long id) {
        OffreEmploi offre = offreEmploiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));
        return mapToResponse(offre);
    }

    // Récupérer les offres d'un étudiant
    public List<OffreEmploiResponse> getOffresByEtudiant(Long etudiantId) {
        List<OffreEmploi> offres = offreEmploiRepository.findByEtudiantId(etudiantId);
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Rechercher des offres par mot-clé
    public List<OffreEmploiResponse> searchOffres(String keyword) {
        List<OffreEmploi> offres = offreEmploiRepository.searchGlobal(keyword);
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Filtrer par localisation
    public List<OffreEmploiResponse> getOffresByLocalisation(String localisation) {
        List<OffreEmploi> offres = offreEmploiRepository.findByLocalisation(localisation);
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Filtrer par type de contrat
    public List<OffreEmploiResponse> getOffresByTypeContrat(String typeContrat) {
        List<OffreEmploi> offres = offreEmploiRepository.findByTypeContrat(typeContrat);
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Mettre à jour une offre
    public OffreEmploiResponse updateOffre(Long id, OffreEmploiRequest request) {
        OffreEmploi offre = offreEmploiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        if (request.getTitre() != null) offre.setTitre(request.getTitre());
        if (request.getEntreprise() != null) offre.setEntreprise(request.getEntreprise());
        if (request.getDescription() != null) offre.setDescription(request.getDescription());
        if (request.getLocalisation() != null) offre.setLocalisation(request.getLocalisation());
        if (request.getTypeContrat() != null) offre.setTypeContrat(request.getTypeContrat());
        if (request.getNiveauExperience() != null) offre.setNiveauExperience(request.getNiveauExperience());
        if (request.getSalaireMin() != null) offre.setSalaireMin(request.getSalaireMin());
        if (request.getSalaireMax() != null) offre.setSalaireMax(request.getSalaireMax());
        if (request.getCompetences() != null) offre.setCompetences(request.getCompetences());
        if (request.getDateExpiration() != null) offre.setDateExpiration(request.getDateExpiration());

        OffreEmploi updated = offreEmploiRepository.save(offre);
        return mapToResponse(updated);
    }

    // Supprimer une offre
    public void deleteOffre(Long id) {
        if (!offreEmploiRepository.existsById(id)) {
            throw new RuntimeException("Offre non trouvée");
        }
        offreEmploiRepository.deleteById(id);
    }

    // Désactiver une offre
    public OffreEmploiResponse desactiverOffre(Long id) {
        OffreEmploi offre = offreEmploiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));
        offre.setActive(false);
        OffreEmploi updated = offreEmploiRepository.save(offre);
        return mapToResponse(updated);
    }

    // Mapper OffreEmploi vers OffreEmploiResponse
    private OffreEmploiResponse mapToResponse(OffreEmploi offre) {
        OffreEmploiResponse response = new OffreEmploiResponse();
        response.setId(offre.getId());
        response.setTitre(offre.getTitre());
        response.setEntreprise(offre.getEntreprise());
        response.setDescription(offre.getDescription());
        response.setLocalisation(offre.getLocalisation());
        response.setTypeContrat(offre.getTypeContrat());
        response.setNiveauExperience(offre.getNiveauExperience());
        response.setSalaireMin(offre.getSalaireMin());
        response.setSalaireMax(offre.getSalaireMax());
        response.setCompetences(offre.getCompetences());
        response.setDatePublication(offre.getDatePublication());
        response.setDateExpiration(offre.getDateExpiration());
        response.setActive(offre.getActive());

        if (offre.getEtudiant() != null) {
            response.setEtudiantId(offre.getEtudiant().getId());
            response.setEtudiantNom(offre.getEtudiant().getNom() + " " + offre.getEtudiant().getPrenom());
        }

        return response;
    }
}