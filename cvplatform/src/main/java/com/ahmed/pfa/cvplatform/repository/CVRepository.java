package com.ahmed.pfa.cvplatform.repository;

import com.ahmed.pfa.cvplatform.model.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CVRepository extends JpaRepository<CV, Long> {

    // Trouver tous les CVs d'un étudiant
    List<CV> findByEtudiantId(Long etudiantId);

    // Trouver les CVs par nom de fichier
    List<CV> findByNomFichier(String nomFichier);

    // Compter les CVs d'un étudiant
    Long countByEtudiantId(Long etudiantId);

    // Vérifier si un CV existe
    boolean existsByNomFichierAndEtudiantId(String nomFichier, Long etudiantId);
}