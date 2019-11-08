package com.backend.devp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountCredentials implements Serializable {
    private String username;
    private String password;
    private Collection<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    @Setter(AccessLevel.NONE)
    private LocalDate loginDate = LocalDate.now();
}
