package com.ahmed.pfa.cvplatform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilitaire pour la gestion des tokens JWT
 * Génère, valide et extrait les informations des tokens
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;

    /**
     * Génère la clé secrète à partir de la configuration
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Génère un token JWT pour un utilisateur
     *
     * @param email Email de l'utilisateur
     * @param userId ID de l'utilisateur
     * @return Token JWT
     */
    public String generateToken(String email, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        logger.info("Token généré pour l'utilisateur: {}", email);
        return token;
    }

    /**
     * Extrait l'email du token
     *
     * @param token Token JWT
     * @return Email de l'utilisateur
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait l'userId du token
     *
     * @param token Token JWT
     * @return ID de l'utilisateur
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extrait la date d'expiration du token
     *
     * @param token Token JWT
     * @return Date d'expiration
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait une information spécifique du token
     *
     * @param token Token JWT
     * @param claimsResolver Function pour extraire le claim
     * @return Valeur du claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrait tous les claims du token
     *
     * @param token Token JWT
     * @return Claims
     * @throws ExpiredJwtException Si le token a expiré
     * @throws MalformedJwtException Si le token est malformé
     * @throws SignatureException Si la signature est invalide
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie si le token a expiré
     *
     * @param token Token JWT
     * @return true si expiré, false sinon
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            logger.warn("Token expiré: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Valide le token JWT
     *
     * @param token Token JWT
     * @param email Email pour vérification
     * @return true si valide, false sinon
     */
    public boolean validateToken(String token, String email) {
        try {
            final String tokenEmail = extractEmail(token);
            boolean isValid = (tokenEmail.equals(email) && !isTokenExpired(token));

            if (isValid) {
                logger.debug("Token valide pour: {}", email);
            } else {
                logger.warn("Token invalide pour: {}", email);
            }

            return isValid;

        } catch (SignatureException e) {
            logger.error("Signature JWT invalide: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token JWT malformé: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expiré: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT non supporté: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims vide: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Obtient le temps restant avant expiration
     *
     * @param token Token JWT
     * @return Temps en millisecondes, -1 si expiré
     */
    public long getTimeUntilExpiration(String token) {
        try {
            Date expiration = extractExpiration(token);
            long timeRemaining = expiration.getTime() - new Date().getTime();
            return Math.max(0, timeRemaining);
        } catch (Exception e) {
            logger.error("Erreur lors du calcul du temps restant: {}", e.getMessage());
            return -1;
        }
    }
}