package com.ahmed.pfa.cvplatform.service;

import com.ahmed.pfa.cvplatform.dto.CVResponse;
import com.ahmed.pfa.cvplatform.dto.CVUploadResponse;
import com.ahmed.pfa.cvplatform.model.CV;
import com.ahmed.pfa.cvplatform.model.Etudiant;
import com.ahmed.pfa.cvplatform.repository.CVRepository;
import com.ahmed.pfa.cvplatform.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CVService {

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private FileStorageService fileStorageService;

    // Upload d'un CV
    public CVUploadResponse uploadCV(MultipartFile file, Long etudiantId) {
        // 1. Vérifier que l'étudiant existe
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        // 2. Vérifier le type de fichier
        String contentType = file.getContentType();
        if (!isValidFileType(contentType)) {
            throw new RuntimeException("Type de fichier non supporté. Utilisez PDF ou Word.");
        }

        // 3. Vérifier la taille (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Fichier trop volumineux. Maximum 5MB.");
        }

        // 4. Sauvegarder le fichier physiquement
        String storedFileName = fileStorageService.storeFile(file);

        // 5. Créer l'entité CV
        CV cv = new CV();
        cv.setNomFichier(file.getOriginalFilename());
        cv.setCheminFichier(storedFileName);
        cv.setTypeFichier(contentType);
        cv.setTailleFichier(file.getSize());
        cv.setDateUpload(LocalDateTime.now());
        cv.setEtudiant(etudiant);

        // TODO Phase 3.5: Extraire le texte du CV
        // cv.setContenuTexte(extractText(file));

        // 6. Sauvegarder dans la base de données
        CV savedCV = cvRepository.save(cv);

        // 7. Retourner la réponse
        return new CVUploadResponse(
                savedCV.getId(),
                savedCV.getNomFichier(),
                savedCV.getTypeFichier(),
                savedCV.getTailleFichier(),
                savedCV.getDateUpload(),
                "CV uploadé avec succès"
        );
    }

    // Récupérer tous les CVs d'un étudiant
    public List<CVResponse> getCVsByEtudiant(Long etudiantId) {
        List<CV> cvs = cvRepository.findByEtudiantId(etudiantId);
        return cvs.stream()
                .map(this::mapToCVResponse)
                .collect(Collectors.toList());
    }

    // Récupérer un CV par ID
    public CVResponse getCVById(Long cvId) {
        CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new RuntimeException("CV non trouvé"));
        return mapToCVResponse(cv);
    }

    // Supprimer un CV
    public void deleteCV(Long cvId) {
        CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new RuntimeException("CV non trouvé"));

        // Supprimer le fichier physique
        fileStorageService.deleteFile(cv.getCheminFichier());

        // Supprimer de la base de données
        cvRepository.delete(cv);
    }

    // Vérifier si le type de fichier est valide
    private boolean isValidFileType(String contentType) {
        return contentType != null && (
                contentType.equals("application/pdf") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        );
    }

    // Mapper CV vers CVResponse
    private CVResponse mapToCVResponse(CV cv) {
        return new CVResponse(
                cv.getId(),
                cv.getNomFichier(),
                cv.getTypeFichier(),
                cv.getTailleFichier(),
                cv.getDateUpload(),
                cv.getEtudiant().getId(),
                cv.getEtudiant().getNom() + " " + cv.getEtudiant().getPrenom(),
                false // analyseDisponible sera implémenté en Phase 5
        );
    }
}