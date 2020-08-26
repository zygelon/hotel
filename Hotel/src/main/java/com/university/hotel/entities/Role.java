package com.university.hotel.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,ADMIN,offline_access,uma_authorization;

    @Override
    public String getAuthority() {
        return name();
    }
}
