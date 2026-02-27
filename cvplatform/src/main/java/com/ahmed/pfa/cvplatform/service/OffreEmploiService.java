package com.ahmed.pfa.cvplatform.service;

import com.ahmed.pfa.cvplatform.dto.OffreEmploiRequest;
import com.ahmed.pfa.cvplatform.dto.OffreEmploiResponse;
import com.ahmed.pfa.cvplatform.exception.ResourceNotFoundException;
import com.ahmed.pfa.cvplatform.model.Etudiant;
import com.ahmed.pfa.cvplatform.model.OffreEmploi;
import com.ahmed.pfa.cvplatform.repository.EtudiantRepository;
import com.ahmed.pfa.cvplatform.repository.OffreEmploiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OffreEmploiService {

    @Autowired
    private OffreEmploiRepository offreEmploiRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Transactional
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

        if (request.getEtudiantId() != null) {
            Etudiant etudiant = etudiantRepository.findById(request.getEtudiantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Étudiant", request.getEtudiantId()));
            offre.setEtudiant(etudiant);
        }

        OffreEmploi saved = offreEmploiRepository.save(offre);
        return mapToResponse(saved);
    }

    // ========== MÉTHODES SANS PAGINATION (anciennes - gardées) ==========

    @Transactional(readOnly = true)
    public List<OffreEmploiResponse> getAllOffresActives() {
        List<OffreEmploi> offres = offreEmploiRepository.findByActiveTrue();
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OffreEmploiResponse> getAllOffres() {
        List<OffreEmploi> offres = offreEmploiRepository.findAll();
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OffreEmploiResponse getOffreById(Long id) {
        OffreEmploi offre = offreEmploiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre", id));
        return mapToResponse(offre);
    }

    @Transactional(readOnly = true)
    public List<OffreEmploiResponse> getOffresByEtudiant(Long etudiantId) {
        List<OffreEmploi> offres = offreEmploiRepository.findByEtudiantId(etudiantId);
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OffreEmploiResponse> searchOffres(String keyword) {
        List<OffreEmploi> offres = offreEmploiRepository.searchGlobal(keyword);
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OffreEmploiResponse> getOffresByLocalisation(String localisation) {
        List<OffreEmploi> offres = offreEmploiRepository.findByLocalisation(localisation);
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OffreEmploiResponse> getOffresByTypeContrat(String typeContrat) {
        List<OffreEmploi> offres = offreEmploiRepository.findByTypeContrat(typeContrat);
        return offres.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ========== NOUVELLES MÉTHODES AVEC PAGINATION ==========

    @Transactional(readOnly = true)
    public Page<OffreEmploiResponse> getAllOffresActivesPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("datePublication").descending());
        Page<OffreEmploi> offresPage = offreEmploiRepository.findByActiveTrue(pageable);
        return offresPage.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<OffreEmploiResponse> getAllOffresPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("datePublication").descending());
        Page<OffreEmploi> offresPage = offreEmploiRepository.findAll(pageable);
        return offresPage.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<OffreEmploiResponse> getOffresByLocalisationPage(String localisation, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("datePublication").descending());
        Page<OffreEmploi> offresPage = offreEmploiRepository.findByLocalisation(localisation, pageable);
        return offresPage.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<OffreEmploiResponse> getOffresByTypeContratPage(String typeContrat, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("datePublication").descending());
        Page<OffreEmploi> offresPage = offreEmploiRepository.findByTypeContrat(typeContrat, pageable);
        return offresPage.map(this::mapToResponse);
    }

    // ========== CRUD OPERATIONS ==========

    @Transactional
    public OffreEmploiResponse updateOffre(Long id, OffreEmploiRequest request) {
        OffreEmploi offre = offreEmploiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre", id));

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

    @Transactional
    public void deleteOffre(Long id) {
        if (!offreEmploiRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offre", id);
        }
        offreEmploiRepository.deleteById(id);
    }

    @Transactional
    public OffreEmploiResponse desactiverOffre(Long id) {
        OffreEmploi offre = offreEmploiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre", id));
        offre.setActive(false);
        OffreEmploi updated = offreEmploiRepository.save(offre);
        return mapToResponse(updated);
    }

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