package com.ahmed.pfa.cvplatform.service;

import com.ahmed.pfa.cvplatform.dto.CVResponse;
import com.ahmed.pfa.cvplatform.dto.CVUploadResponse;
import com.ahmed.pfa.cvplatform.exception.InvalidFileException;
import com.ahmed.pfa.cvplatform.exception.ResourceNotFoundException;
import com.ahmed.pfa.cvplatform.model.CV;
import com.ahmed.pfa.cvplatform.model.Etudiant;
import com.ahmed.pfa.cvplatform.repository.CVRepository;
import com.ahmed.pfa.cvplatform.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // =========================================================================
    // ✅ GESTION DES FICHIERS (UPLOAD / DELETE)
    // =========================================================================

    /**
     * Upload d'un CV
     * @Transactional: Si erreur après save file -> rollback DB
     */
    @Transactional
    public CVUploadResponse uploadCV(MultipartFile file, Long etudiantId) {
        // 1. Vérifier que l'étudiant existe
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", etudiantId));

        // 2. Vérifier le type de fichier
        String contentType = file.getContentType();
        if (!isValidFileType(contentType)) {
            throw new InvalidFileException("Type de fichier non supporté. Utilisez PDF ou Word.");
        }

        // 3. Vérifier la taille (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new InvalidFileException("Fichier trop volumineux. Maximum 5MB.");
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

        // 6. Sauvegarder dans la base de données
        CV savedCV = cvRepository.save(cv);

        return new CVUploadResponse(
                savedCV.getId(),
                savedCV.getNomFichier(),
                savedCV.getTypeFichier(),
                savedCV.getTailleFichier(),
                savedCV.getDateUpload(),
                "CV uploadé avec succès"
        );
    }

    /**
     * Supprimer un CV
     * @Transactional: Garantit suppression atomique (DB + File)
     */
    @Transactional
    public void deleteCV(Long cvId) {
        CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("CV", cvId));

        // Supprimer le fichier physique
        fileStorageService.deleteFile(cv.getCheminFichier());

        // Supprimer de la base de données
        cvRepository.delete(cv);
    }

    // =========================================================================
    // ✅ NOUVELLE MÉTHODE AVEC PAGINATION
    // =========================================================================

    /**
     * Récupérer tous les CVs d'un étudiant avec pagination
     * @Transactional(readOnly = true): Optimisation lecture seule
     */
    @Transactional(readOnly = true)
    public Page<CVResponse> getCVsByEtudiantPage(Long etudiantId, int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("dateUpload").descending()
        );

        Page<CV> cvsPage = cvRepository.findByEtudiantId(etudiantId, pageable);
        return cvsPage.map(this::mapToCVResponse);
    }

    // =========================================================================
    // ✅ MÉTHODES DE LECTURE CLASSIQUES (READ ONLY)
    // =========================================================================

    @Transactional(readOnly = true)
    public List<CVResponse> getCVsByEtudiant(Long etudiantId) {
        List<CV> cvs = cvRepository.findByEtudiantId(etudiantId);
        return cvs.stream()
                .map(this::mapToCVResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CVResponse getCVById(Long cvId) {
        CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("CV", cvId));
        return mapToCVResponse(cv);
    }

    // =========================================================================
    // ✅ HELPERS
    // =========================================================================

    private boolean isValidFileType(String contentType) {
        return contentType != null && (
                contentType.equals("application/pdf") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        );
    }

    private CVResponse mapToCVResponse(CV cv) {
        return new CVResponse(
                cv.getId(),
                cv.getNomFichier(),
                cv.getTypeFichier(),
                cv.getTailleFichier(),
                cv.getDateUpload(),
                cv.getEtudiant().getId(),
                cv.getEtudiant().getNom() + " " + cv.getEtudiant().getPrenom(),
                false
        );
    }
}