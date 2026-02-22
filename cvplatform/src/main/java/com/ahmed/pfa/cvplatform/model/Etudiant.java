package com.ahmed.pfa.cvplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "etudiant")
@DiscriminatorValue("ETUDIANT")
public class Etudiant extends Utilisateur {

    @Column(name = "niveau_etude")
    private String niveauEtude; // Ex: "Licence", "Master", "Doctorat"

    @Column(name = "domaine_etude")
    private String domaineEtude; // Ex: "Informatique", "Gestion"

    private String universite;

    // TODO: Décommenter en Phase 3 quand CV sera créé
    // @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<CV> cvs = new ArrayList<>();

    // TODO: Décommenter en Phase 3 quand OffreEmploi sera créé
    // @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<OffreEmploi> offresEmploi = new ArrayList<>();
}