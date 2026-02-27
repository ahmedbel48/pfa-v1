package com.ahmed.pfa.cvplatform.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Gère les tentatives d'accès non authentifiées
 * Retourne 401 au lieu de 403
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        logger.warn("Tentative d'accès non autorisée: {}", request.getRequestURI());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Créer JSON manuellement (sans ObjectMapper)
        String jsonResponse = String.format(
                "{\"message\":\"Authentification requise. Veuillez fournir un token JWT valide.\",\"status\":401,\"timestamp\":\"%s\",\"path\":\"%s\"}",
                LocalDateTime.now().toString(),
                request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
    }
}