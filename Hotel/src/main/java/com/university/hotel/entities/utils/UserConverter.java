package com.university.hotel.entities.utils;

import com.university.hotel.entities.Role;
import com.university.hotel.entities.User;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

public class UserConverter {
    public static User getUserFromKeycloakPrincipal(Principal p){
        User user = new User();
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) p;
        KeycloakPrincipal principal=(KeycloakPrincipal)token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        user.setUsername(accessToken.getPreferredUsername());
        user.setEmail(accessToken.getEmail());
        user.setFirstName(accessToken.getGivenName());
        user.setLastName(accessToken.getFamilyName());
        AccessToken.Access realmAccess = accessToken.getRealmAccess();
        Set<String> roles = realmAccess.getRoles();
        Set<Role> userRoles = new HashSet<>();
        for(String role:roles){
            userRoles.add(Role.valueOf(role));
        }
        user.setRoles(userRoles);
        return user;
    }
}
