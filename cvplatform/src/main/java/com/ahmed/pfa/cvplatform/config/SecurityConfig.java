package com.ahmed.pfa.cvplatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuration de sécurité Spring Security
 * Gère l'authentification, l'autorisation et CORS
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    /**
     * Configuration du PasswordEncoder pour le hachage des mots de passe
     * Utilise BCrypt avec un facteur de coût de 10 (par défaut)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration de la chaîne de filtres de sécurité
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configuration CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Désactiver CSRF (pas nécessaire pour REST API stateless)
                .csrf(csrf -> csrf.disable())

                // Politique de session: stateless (utilisation de JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configuration des autorisations
                .authorizeHttpRequests(auth -> auth
                        // Endpoints publics (authentication)
                        .requestMatchers("/api/v1/auth/**", "/api/auth/**").permitAll()

                        // Swagger/OpenAPI documentation
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Actuator endpoints (monitoring)
                        .requestMatchers("/actuator/**").permitAll()

                        // Tous les autres endpoints
                        // TODO: Activer l'authentification en production
                        // .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        // .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().permitAll()
                )

                // Désactiver les formulaires de login par défaut
                .formLogin(form -> form.disable())

                // Désactiver HTTP Basic authentication
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}