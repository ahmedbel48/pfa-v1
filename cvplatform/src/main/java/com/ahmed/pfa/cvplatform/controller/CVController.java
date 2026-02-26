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
public class CVController {

    @Autowired
    private CVService cvService;

    /**
     * Upload d'un CV
     */
    @PostMapping("/upload")
    public ResponseEntity<CVUploadResponse> uploadCV(
            @RequestParam("file") MultipartFile file,
            @RequestParam("etudiantId") Long etudiantId) {
        CVUploadResponse response = cvService.uploadCV(file, etudiantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupérer tous les CVs d'un étudiant
     */
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<CVResponse>> getCVsByEtudiant(@PathVariable Long etudiantId) {
        List<CVResponse> cvs = cvService.getCVsByEtudiant(etudiantId);
        return ResponseEntity.ok(cvs);
    }

    /**
     * Récupérer un CV par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CVResponse> getCVById(@PathVariable Long id) {
        CVResponse cv = cvService.getCVById(id);
        return ResponseEntity.ok(cv);
    }

    /**
     * Supprimer un CV
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCV(@PathVariable Long id) {
        cvService.deleteCV(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint de test
     */
    @GetMapping("/test")
    public String test() {
        return "CV Controller fonctionne!";
    }
}