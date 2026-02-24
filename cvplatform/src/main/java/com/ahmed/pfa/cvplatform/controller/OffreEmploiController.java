package com.ahmed.pfa.cvplatform.controller;

import com.ahmed.pfa.cvplatform.dto.OffreEmploiRequest;
import com.ahmed.pfa.cvplatform.dto.OffreEmploiResponse;
import com.ahmed.pfa.cvplatform.service.OffreEmploiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class OffreEmploiController {

    @Autowired
    private OffreEmploiService offreEmploiService;

    // Créer une offre d'emploi
    @PostMapping
    public ResponseEntity<OffreEmploiResponse> createOffre(@RequestBody OffreEmploiRequest request) {
        try {
            OffreEmploiResponse response = offreEmploiService.createOffre(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Récupérer toutes les offres actives
    @GetMapping("/active")
    public ResponseEntity<List<OffreEmploiResponse>> getAllOffresActives() {
        List<OffreEmploiResponse> offres = offreEmploiService.getAllOffresActives();
        return ResponseEntity.ok(offres);
    }

    // Récupérer toutes les offres
    @GetMapping
    public ResponseEntity<List<OffreEmploiResponse>> getAllOffres() {
        List<OffreEmploiResponse> offres = offreEmploiService.getAllOffres();
        return ResponseEntity.ok(offres);
    }

    // Récupérer une offre par ID
    @GetMapping("/{id}")
    public ResponseEntity<OffreEmploiResponse> getOffreById(@PathVariable Long id) {
        try {
            OffreEmploiResponse offre = offreEmploiService.getOffreById(id);
            return ResponseEntity.ok(offre);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer les offres d'un étudiant
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<OffreEmploiResponse>> getOffresByEtudiant(@PathVariable Long etudiantId) {
        List<OffreEmploiResponse> offres = offreEmploiService.getOffresByEtudiant(etudiantId);
        return ResponseEntity.ok(offres);
    }

    // Rechercher des offres
    @GetMapping("/search")
    public ResponseEntity<List<OffreEmploiResponse>> searchOffres(@RequestParam String keyword) {
        List<OffreEmploiResponse> offres = offreEmploiService.searchOffres(keyword);
        return ResponseEntity.ok(offres);
    }

    // Filtrer par localisation
    @GetMapping("/localisation/{localisation}")
    public ResponseEntity<List<OffreEmploiResponse>> getOffresByLocalisation(@PathVariable String localisation) {
        List<OffreEmploiResponse> offres = offreEmploiService.getOffresByLocalisation(localisation);
        return ResponseEntity.ok(offres);
    }

    // Filtrer par type de contrat
    @GetMapping("/contrat/{typeContrat}")
    public ResponseEntity<List<OffreEmploiResponse>> getOffresByTypeContrat(@PathVariable String typeContrat) {
        List<OffreEmploiResponse> offres = offreEmploiService.getOffresByTypeContrat(typeContrat);
        return ResponseEntity.ok(offres);
    }

    // Mettre à jour une offre
    @PutMapping("/{id}")
    public ResponseEntity<OffreEmploiResponse> updateOffre(
            @PathVariable Long id,
            @RequestBody OffreEmploiRequest request) {
        try {
            OffreEmploiResponse response = offreEmploiService.updateOffre(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Désactiver une offre
    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<OffreEmploiResponse> desactiverOffre(@PathVariable Long id) {
        try {
            OffreEmploiResponse response = offreEmploiService.desactiverOffre(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une offre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffre(@PathVariable Long id) {
        try {
            offreEmploiService.deleteOffre(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint de test
    @GetMapping("/test")
    public String test() {
        return "Job Offers Controller fonctionne!";
    }
}
