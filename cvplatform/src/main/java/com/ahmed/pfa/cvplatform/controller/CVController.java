package com.ahmed.pfa.cvplatform.controller;

import com.ahmed.pfa.cvplatform.dto.CVResponse;
import com.ahmed.pfa.cvplatform.dto.CVUploadResponse;
import com.ahmed.pfa.cvplatform.service.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cv")
// ✅ @CrossOrigin(origins = "*") supprimé pour la sécurité
public class CVController {

    @Autowired
    private CVService cvService;

    // Upload d'un CV
    @PostMapping("/upload")
    public ResponseEntity<CVUploadResponse> uploadCV(
            @RequestParam("file") MultipartFile file,
            @RequestParam("etudiantId") Long etudiantId) {
        try {
            CVUploadResponse response = cvService.uploadCV(file, etudiantId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new CVUploadResponse(null, null, null, null, null, e.getMessage()));
        }
    }

    // Récupérer tous les CVs d'un étudiant
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<CVResponse>> getCVsByEtudiant(@PathVariable Long etudiantId) {
        List<CVResponse> cvs = cvService.getCVsByEtudiant(etudiantId);
        return ResponseEntity.ok(cvs);
    }

    // Récupérer un CV par ID
    @GetMapping("/{id}")
    public ResponseEntity<CVResponse> getCVById(@PathVariable Long id) {
        try {
            CVResponse cv = cvService.getCVById(id);
            return ResponseEntity.ok(cv);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un CV
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCV(@PathVariable Long id) {
        try {
            cvService.deleteCV(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint de test
    @GetMapping("/test")
    public String test() {
        return "CV Controller fonctionne!";
    }
}