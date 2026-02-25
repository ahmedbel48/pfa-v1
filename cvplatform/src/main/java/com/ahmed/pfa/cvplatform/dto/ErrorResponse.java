package com.ahmed.pfa.cvplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO pour les réponses d'erreur standardisées
 * Utilisé par le GlobalExceptionHandler
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    /**
     * Message d'erreur principal
     */
    private String message;

    /**
     * Code d'erreur HTTP (400, 404, 500, etc.)
     */
    private int status;

    /**
     * Timestamp de l'erreur
     */
    private LocalDateTime timestamp;

    /**
     * Chemin de la requête qui a causé l'erreur
     */
    private String path;

    /**
     * Détails additionnels (optionnel)
     * Utilisé pour les erreurs de validation (field -> message)
     */
    private Map<String, String> details;

    /**
     * Constructeur sans détails
     */
    public ErrorResponse(String message, int status, LocalDateTime timestamp, String path) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.path = path;
    }
}