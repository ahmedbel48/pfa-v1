package com.ahmed.pfa.cvplatform.controller;

import com.ahmed.pfa.cvplatform.dto.OffreEmploiRequest;
import com.ahmed.pfa.cvplatform.dto.OffreEmploiResponse;
import com.ahmed.pfa.cvplatform.service.OffreEmploiService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class OffreEmploiController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OffreEmploiController.class);

    @Autowired
    private OffreEmploiService offreEmploiService;

    // =========================================================================
    // ✅ CRÉATION ET MISE À JOUR
    // =========================================================================

    @PostMapping
    public ResponseEntity<OffreEmploiResponse> createOffre(@Valid @RequestBody OffreEmploiRequest request) {
        logger.info("User {} crée offre: {}", getAuthenticatedUserEmail(), request.getTitre());
        OffreEmploiResponse response = offreEmploiService.createOffre(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OffreEmploiResponse> updateOffre(
            @PathVariable Long id,
            @Valid @RequestBody OffreEmploiRequest request) {
        OffreEmploiResponse response = offreEmploiService.updateOffre(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<OffreEmploiResponse> desactiverOffre(@PathVariable Long id) {
        OffreEmploiResponse response = offreEmploiService.desactiverOffre(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffre(@PathVariable Long id) {
        offreEmploiService.deleteOffre(id);
        return ResponseEntity.ok().build();
    }

    // =========================================================================
    // ✅ NOUVEAUX ENDPOINTS AVEC PAGINATION (PAGEABLE)
    // =========================================================================

    /**
     * Récupérer toutes les offres actives avec pagination
     */
    @GetMapping("/active/page")
    public ResponseEntity<Page<OffreEmploiResponse>> getAllOffresActivesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<OffreEmploiResponse> offres = offreEmploiService.getAllOffresActivesPage(page, size);
        return ResponseEntity.ok(offres);
    }

    /**
     * Récupérer toutes les offres avec pagination
     */
    @GetMapping("/page")
    public ResponseEntity<Page<OffreEmploiResponse>> getAllOffresPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<OffreEmploiResponse> offres = offreEmploiService.getAllOffresPage(page, size);
        return ResponseEntity.ok(offres);
    }

    /**
     * Filtrer par localisation avec pagination
     */
    @GetMapping("/localisation/{localisation}/page")
    public ResponseEntity<Page<OffreEmploiResponse>> getOffresByLocalisationPage(
            @PathVariable String localisation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<OffreEmploiResponse> offres = offreEmploiService.getOffresByLocalisationPage(localisation, page, size);
        return ResponseEntity.ok(offres);
    }

    /**
     * Filtrer par type de contrat avec pagination
     */
    @GetMapping("/contrat/{typeContrat}/page")
    public ResponseEntity<Page<OffreEmploiResponse>> getOffresByTypeContratPage(
            @PathVariable String typeContrat,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<OffreEmploiResponse> offres = offreEmploiService.getOffresByTypeContratPage(typeContrat, page, size);
        return ResponseEntity.ok(offres);
    }

    // =========================================================================
    // ✅ ENDPOINTS CLASSIQUES (SANS PAGINATION)
    // =========================================================================

    @GetMapping("/active")
    public ResponseEntity<List<OffreEmploiResponse>> getAllOffresActives() {
        List<OffreEmploiResponse> offres = offreEmploiService.getAllOffresActives();
        return ResponseEntity.ok(offres);
    }

    @GetMapping
    public ResponseEntity<List<OffreEmploiResponse>> getAllOffres() {
        List<OffreEmploiResponse> offres = offreEmploiService.getAllOffres();
        return ResponseEntity.ok(offres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffreEmploiResponse> getOffreById(@PathVariable Long id) {
        OffreEmploiResponse offre = offreEmploiService.getOffreById(id);
        return ResponseEntity.ok(offre);
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<OffreEmploiResponse>> getOffresByEtudiant(@PathVariable Long etudiantId) {
        List<OffreEmploiResponse> offres = offreEmploiService.getOffresByEtudiant(etudiantId);
        return ResponseEntity.ok(offres);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OffreEmploiResponse>> searchOffres(@RequestParam String keyword) {
        List<OffreEmploiResponse> offres = offreEmploiService.searchOffres(keyword);
        return ResponseEntity.ok(offres);
    }

    @GetMapping("/localisation/{localisation}")
    public ResponseEntity<List<OffreEmploiResponse>> getOffresByLocalisation(@PathVariable String localisation) {
        List<OffreEmploiResponse> offres = offreEmploiService.getOffresByLocalisation(localisation);
        return ResponseEntity.ok(offres);
    }

    @GetMapping("/contrat/{typeContrat}")
    public ResponseEntity<List<OffreEmploiResponse>> getOffresByTypeContrat(@PathVariable String typeContrat) {
        List<OffreEmploiResponse> offres = offreEmploiService.getOffresByTypeContrat(typeContrat);
        return ResponseEntity.ok(offres);
    }

    @GetMapping("/test")
    public String test() {
        return "Job Offers Controller fonctionne!";
    }
}