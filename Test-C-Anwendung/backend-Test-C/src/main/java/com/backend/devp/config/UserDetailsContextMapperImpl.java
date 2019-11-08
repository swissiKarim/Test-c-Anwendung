package com.backend.devp.config;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsContextMapperImpl implements UserDetailsContextMapper, Serializable {
    @Override
    public UserDetails mapUserFromContext(DirContextOperations dirContextOperations, String username, Collection<? extends GrantedAuthority> authority) {
        List<GrantedAuthority> mappedAuthorities = new ArrayList<>();

        for (GrantedAuthority granted : authority) {

            if (granted.getAuthority().equalsIgnoreCase("managers")) {
                mappedAuthorities.add(() -> "ROLE_MANAGER");
            } else if (granted.getAuthority().equalsIgnoreCase("developers")) {
                mappedAuthorities.add(() -> "ROLE_DEVELOPER");
            } else if (granted.getAuthority().equalsIgnoreCase("submanagers")) {
                mappedAuthorities.add(() -> "ROLE_SUBMANAGER");
            }
        }
        return new User(username, "", true, true, true, true, mappedAuthorities);
    }

    @Override
    public void mapUserToContext(UserDetails userDetails, DirContextAdapter dirContextAdapter) {

    }
}
