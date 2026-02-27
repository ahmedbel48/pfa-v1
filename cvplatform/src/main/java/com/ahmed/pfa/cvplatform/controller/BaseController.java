package com.ahmed.pfa.cvplatform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Controller de base avec méthodes utilitaires communes
 * Tous les controllers héritent de cette classe
 */
public abstract class BaseController {

    /**
     * Récupère l'email de l'utilisateur authentifié
     *
     * @return Email de l'utilisateur ou null si non authentifié
     */
    protected String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            return authentication.getName();
        }

        return null;
    }

    /**
     * Vérifie si un utilisateur est authentifié
     *
     * @return true si authentifié, false sinon
     */
    protected boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"));
    }

    /**
     * Récupère l'objet Authentication complet
     *
     * @return Authentication object ou null
     */
    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}