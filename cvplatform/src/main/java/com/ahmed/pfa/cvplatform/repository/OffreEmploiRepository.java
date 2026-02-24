package com.ahmed.pfa.cvplatform.repository;

import com.ahmed.pfa.cvplatform.model.OffreEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Long> {

    // Trouver les offres actives
    List<OffreEmploi> findByActiveTrue();

    // Trouver les offres par entreprise
    List<OffreEmploi> findByEntreprise(String entreprise);

    // Trouver les offres par localisation
    List<OffreEmploi> findByLocalisation(String localisation);

    // Trouver les offres par type de contrat
    List<OffreEmploi> findByTypeContrat(String typeContrat);

    // Trouver les offres d'un étudiant
    List<OffreEmploi> findByEtudiantId(Long etudiantId);

    // Recherche par titre (contient)
    @Query("SELECT o FROM OffreEmploi o WHERE LOWER(o.titre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<OffreEmploi> searchByTitre(@Param("keyword") String keyword);

    // Recherche globale
    @Query("SELECT o FROM OffreEmploi o WHERE " +
            "LOWER(o.titre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.entreprise) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(o.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<OffreEmploi> searchGlobal(@Param("keyword") String keyword);
}