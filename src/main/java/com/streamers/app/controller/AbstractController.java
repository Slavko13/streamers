package com.streamers.app.controller;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;




public abstract class AbstractController {

    protected AccessToken getKeycloakAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
            return keycloakPrincipal.getKeycloakSecurityContext().getToken();
        }
        return null;
    }

    protected KeycloakSecurityContext getKeycloakSecurityContext() {
        AccessToken token = getKeycloakAccessToken();
        if (token != null) {
            return (KeycloakSecurityContext) token.getOtherClaims().get("realm_access");
        }
        return null;
    }

    protected String getCurrentUsername() {
        AccessToken token = getKeycloakAccessToken();
        return token != null ? token.getPreferredUsername() : null;
    }

    // Добавьте другие методы по мере необходимости
}


