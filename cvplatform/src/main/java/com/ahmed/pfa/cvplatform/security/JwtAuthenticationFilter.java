package com.ahmed.pfa.cvplatform.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Filtre JWT pour authentifier les requêtes
 * Vérifie le token dans le header Authorization
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Extraire le token du header Authorization
            String token = extractTokenFromRequest(request);

            if (token != null) {
                // 2. Extraire l'email du token
                String email = jwtUtil.extractEmail(token);

                // 3. Vérifier que l'utilisateur n'est pas déjà authentifié
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 4. Valider le token
                    if (jwtUtil.validateToken(token, email)) {

                        // 5. Extraire userId et créer authentication
                        Long userId = jwtUtil.extractUserId(token);

                        // 6. Créer les authorities (rôles)
                        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_USER")
                        );

                        // 7. Créer authentication token
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        );

                        // 8. Ajouter détails de la requête
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 9. Mettre dans SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        logger.debug("Utilisateur authentifié: {}", email);
                    } else {
                        logger.warn("Token invalide pour: {}", email);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification JWT: {}", e.getMessage());
        }

        // 10. Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token du header Authorization
     * Format attendu: "Bearer <token>"
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Enlever "Bearer "
        }

        return null;
    }
}