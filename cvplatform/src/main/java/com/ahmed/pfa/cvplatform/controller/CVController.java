package com.ahmed.pfa.cvplatform.controller;

import com.ahmed.pfa.cvplatform.dto.CVResponse;
import com.ahmed.pfa.cvplatform.dto.CVUploadResponse;
import com.ahmed.pfa.cvplatform.service.CVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cv")
public class CVController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CVController.class);

    @Autowired
    private CVService cvService;

    @PostMapping("/upload")
    public ResponseEntity<CVUploadResponse> uploadCV(
            @RequestParam("file") MultipartFile file,
            @RequestParam("etudiantId") Long etudiantId) {

        logger.info("User {} upload CV pour étudiant {}", getAuthenticatedUserEmail(), etudiantId);
        CVUploadResponse response = cvService.uploadCV(file, etudiantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<CVResponse>> getCVsByEtudiant(@PathVariable Long etudiantId) {
        List<CVResponse> cvs = cvService.getCVsByEtudiant(etudiantId);
        return ResponseEntity.ok(cvs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CVResponse> getCVById(@PathVariable Long id) {
        CVResponse cv = cvService.getCVById(id);
        return ResponseEntity.ok(cv);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCV(@PathVariable Long id) {
        cvService.deleteCV(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public String test() {
        return "CV Controller fonctionne!";
    }
}